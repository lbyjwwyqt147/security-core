package pers.liujunyi.cloud.security.service.authorization.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaMongoServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.security.domain.authorization.RoleInfoDto;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.repository.jpa.authorization.RoleInfoRepository;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleResourceMongoRepository;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleUserMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleInfoMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleInfoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: RoleInfoServiceImpl.java
 * 文件描述: 角色 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleInfoServiceImpl extends BaseJpaMongoServiceImpl<RoleInfo, Long> implements RoleInfoService {

    @Autowired
    private RoleInfoRepository roleInfoRepository;
    @Autowired
    private RoleInfoMongoService roleInfoMongoService;
    @Autowired
    private RoleUserMongoRepository roleUserMongoRepository;
    @Autowired
    private RoleResourceMongoRepository roleResourceMongoRepository;

    public RoleInfoServiceImpl(BaseJpaRepository<RoleInfo, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(RoleInfoDto record) {
        boolean add = record.getId() == null ? true : false;
        if (this.checkRoleNumberRepetition(record.getRoleNumber(), record.getId())) {
            return ResultUtil.params("角色编号重复,请重新输入！");
        }
        RoleInfo roleInfo = DozerBeanMapperUtil.copyProperties(record, RoleInfo.class);
        if (record.getRoleStatus() == null) {
            roleInfo.setRoleStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (!add) {
            roleInfo.setDataVersion(roleInfo.getDataVersion() + 1);
        } else {
            roleInfo.setDataVersion(1L);
        }
        if (record.getParentId().longValue() > 0) {
            RoleInfo parent = this.findById(record.getParentId());
            roleInfo.setFullRoleParent(parent.getFullRoleParent() + ":"  + parent.getId());
            String curFullParentCode = StringUtils.isNotBlank(parent.getFullRoleParentCode()) && !parent.getFullRoleParentCode().equals("0") ? parent.getFullRoleParentCode() + ":" + record.getRoleNumber() : parent.getRoleNumber();
            roleInfo.setFullRoleParentCode(curFullParentCode);
        } else {
            roleInfo.setFullRoleParent("0");
        }
        RoleInfo saveObject = this.roleInfoRepository.save(roleInfo);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }

        this.roleInfoMongoService.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        if (status.byteValue() == 1) {
            ResultInfo resultInfo = this.checkUseCondition(ids, "禁用");
            if (!resultInfo.getSuccess()) {
                return resultInfo;
            }
        }
        int count = this.roleInfoRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<Long, Map<String, Object>> sourceMap = new ConcurrentHashMap<>(ids.size());
            ids.stream().forEach(item -> {
                Map<String, Object> docDataMap = new HashMap<>(2);
                docDataMap.put("roleStatus", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                sourceMap.put(item, docDataMap);
            });
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        ResultInfo resultInfo = this.checkUseCondition(ids, "删除");
        if (!resultInfo.getSuccess()) {
            return resultInfo;
        }
        long count = this.roleInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.roleInfoMongoService.deleteAllByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToMongo() {
        this.syncDataMongoDb();
        return ResultUtil.success();
    }

    /**
     * 检测角色编号是否重复
     * @param roleNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkRoleNumberRepetition(String roleNumber, Long id) {
        if (id == null){
            return this.checkRoleNumberData(roleNumber);
        } else {
            RoleInfo roleInfo = this.findById(id);
            if (roleInfo != null && !roleInfo.getRoleNumber().equals(roleNumber)) {
                return this.checkRoleNumberData(roleNumber);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在roleNumber 数据
     * @param roleNumber
     * @return
     */
    private Boolean checkRoleNumberData (String roleNumber) {
        RoleInfo roleInfo = this.roleInfoMongoService.findFirstByRoleNumber(roleNumber);
        if (roleInfo != null) {
            return true;
        }
        return false;
    }


    /**
     * 检测数据是否被系统使用
     * @param ids
     * @return
     */
    private ResultInfo checkUseCondition(List<Long> ids, String title) {
       List<RoleResource> list = this.roleResourceMongoRepository.findByRoleIdInAndStatus(ids, null);
       String msg = "要"+title+"的角色正在被系统使用,不能被"+title+"";
       if (!CollectionUtils.isEmpty(list)) {
           return ResultUtil.params(msg);
       }
        List<RoleUser> rolelist = this.roleUserMongoRepository.findByRoleIdInAndStatus(ids, null);
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params(msg);
        }
        return ResultUtil.success();
    }
}

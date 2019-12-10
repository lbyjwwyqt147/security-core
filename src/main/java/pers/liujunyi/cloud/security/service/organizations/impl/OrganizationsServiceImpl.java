package pers.liujunyi.cloud.security.service.organizations.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.repository.jpa.organizations.OrganizationsRepository;
import pers.liujunyi.cloud.security.repository.mongo.organizations.StaffOrgMongoRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsMongoService;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: OrganizationsServiceImpl.java
 * 文件描述: 组织结构 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsServiceImpl extends BaseServiceImpl<Organizations, Long> implements OrganizationsService {

    @Autowired
    private OrganizationsRepository organizationsRepository;
    @Autowired
    private OrganizationsMongoService organizationsMongoService;
    @Autowired
    private StaffOrgMongoRepository staffOrgMongoRepository;

    public OrganizationsServiceImpl(BaseRepository<Organizations, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(OrganizationsDto record) {
        boolean add = record.getId() == null ? true : false;
        if (this.checkOrgNumberRepetition(record.getOrgNumber(), record.getId())) {
            return ResultUtil.params("结构代码重复,请重新输入！");
        }
        Organizations organizations = DozerBeanMapperUtil.copyProperties(record, Organizations.class);
        if (record.getSeq() == null) {
            organizations.setSeq(10);
        }
        if (record.getOrgStatus() == null) {
            organizations.setOrgStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (record.getParentId().longValue() > 0) {
            Organizations parent = this.getOrganizations(record.getParentId());
            organizations.setFullParent(parent.getFullParent() + ":"  + parent.getId());
            organizations.setOrgLevel((byte)(parent.getOrgLevel() + 1));
            String curFullParentCode = StringUtils.isNotBlank(parent.getFullParentCode()) && !parent.getFullParentCode().equals("0") ? parent.getFullParentCode() + ":" + record.getOrgNumber() : parent.getOrgNumber();
            organizations.setFullParentCode(curFullParentCode);
        } else {
            organizations.setFullParent("0");
            organizations.setOrgLevel((byte) 1);
        }
        organizations.setFullName(record.getOrgName());
        Organizations saveObject = this.organizationsRepository.save(organizations);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObject.setDataVersion(saveObject.getDataVersion() + 1);
        } else {
            saveObject.setDataVersion(1L);
        }
        this.organizationsMongoService.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids, String putParams) {
        if (status.byteValue() == 1) {
            ResultInfo resultInfo = this.checkUseCondition(ids, "禁用");
            if (!resultInfo.getSuccess()) {
                return resultInfo;
            }
        }
        int count = this.organizationsRepository.setOrgStatusByIds(status, new Date(), ids);
        if (count > 0) {
            JSONArray jsonArray = JSONArray.parseArray(putParams);
            int jsonSize = jsonArray.size();
            Map<Long, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            for(int i = 0; i < jsonSize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> docDataMap = new HashMap<>();
                docDataMap.put("orgStatus", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                docDataMap.put("dataVersion", jsonObject.getLongValue("dataVersion") + 1);
                sourceMap.put(jsonObject.getLongValue("id"), docDataMap);
            }
            // 更新 Mongo 中的数据
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
        long count = this.organizationsRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.organizationsMongoService.deleteAllByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }


    @Override
    public ResultInfo syncDataToMongo() {
        super.syncDataMongoDb();
        return ResultUtil.success();
    }


    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        return this.organizationsMongoService.getOne(id);
    }


    /**
     * 检测结构代码是否重复
     * @param orgNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkOrgNumberRepetition(String orgNumber, Long id) {
        if (id == null){
            return this.checkOrgNumberData(orgNumber);
        } else {
            Organizations organizations = this.getOrganizations(id);
            if (organizations != null && !organizations.getOrgNumber().equals(orgNumber)) {
                return this.checkOrgNumberData(orgNumber);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在orgNumber 数据
     * @param orgNumber
     * @return
     */
    private Boolean checkOrgNumberData (String orgNumber) {
        Organizations organizations = this.organizationsMongoService.findFirstByOrgNumber(orgNumber);
        if (organizations != null) {
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
        List<StaffOrg> list = this.staffOrgMongoRepository.findByOrgIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params("要"+title+"的角色正在被系统使用,不能被"+title+"");
        }
        return ResultUtil.success();
    }

}

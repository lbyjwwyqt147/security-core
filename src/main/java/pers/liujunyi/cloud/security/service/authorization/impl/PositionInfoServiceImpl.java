package pers.liujunyi.cloud.security.service.authorization.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaMongoServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;
import pers.liujunyi.cloud.security.repository.jpa.authorization.PositionInfoRepository;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoMongoService;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: PositionInfoServiceImpl.java
 * 文件描述: 岗位信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class PositionInfoServiceImpl extends BaseJpaMongoServiceImpl<PositionInfo, Long> implements PositionInfoService {

    @Autowired
    private PositionInfoRepository positionInfoRepository;
    @Autowired
    private PositionInfoMongoService positionInfoMongoService;

    public PositionInfoServiceImpl(BaseJpaRepository<PositionInfo, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(PositionInfoDto record) {
        boolean add = record.getId() == null ? true : false;
        if (this.checkPostNumberRepetition(record.getPostNumber(), record.getId())) {
            return ResultUtil.params("岗位编号重复,请重新输入！");
        }
        PositionInfo positionInfo = DozerBeanMapperUtil.copyProperties(record, PositionInfo.class);
        if (record.getPostStatus() == null) {
            positionInfo.setPostStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (!add) {
            positionInfo.setDataVersion(positionInfo.getDataVersion() + 1);
        } else {
            positionInfo.setDataVersion(1L);
        }
        if (record.getParentId().longValue() > 0) {
            PositionInfo parent = this.findById(record.getParentId());
            positionInfo.setFullPostParent(parent.getFullPostParent() + ":"  + parent.getId());
            positionInfo.setPostLevel((byte)(parent.getPostLevel() + 1));
            String curFullParentCode = StringUtils.isNotBlank(parent.getFullPostParentCode()) && !parent.getFullPostParentCode().equals("0") ? parent.getFullPostParentCode() + ":" + record.getPostNumber() : parent.getPostNumber();
            positionInfo.setFullPostParentCode(curFullParentCode);
        } else {
            positionInfo.setFullPostParent("0");
            positionInfo.setPostLevel((byte) 1);
        }
        positionInfo.setFullPostName(record.getPostName());
        PositionInfo saveObject = this.positionInfoRepository.save(positionInfo);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }

        this.positionInfoMongoService.save(saveObject);
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
        int count = this.positionInfoRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<Long, Map<String, Object>> sourceMap = new ConcurrentHashMap<>(ids.size());
            ids.stream().forEach(item -> {
                Map<String, Object> docDataMap = new HashMap<>(2);
                docDataMap.put("postStatus", status);
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
        long count = this.positionInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.positionInfoMongoService.deleteAllByIdIn(ids);
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
     * 检测岗位编号是否重复
     * @param postNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkPostNumberRepetition(String postNumber, Long id) {
        if (id == null){
            return this.checkPostNumberData(postNumber);
        } else {
            PositionInfo positionInfo = this.findById(id);
            if (positionInfo != null && !positionInfo.getPostNumber().equals(postNumber)) {
                return this.checkPostNumberData(postNumber);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在postNumber 数据
     * @param postNumber
     * @return
     */
    private Boolean checkPostNumberData (String postNumber) {
        PositionInfo postResource = this.positionInfoMongoService.findFirstByPostNumber(postNumber);
        if (postResource != null) {
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
      /*  List<RoleResource> list = this.roleResourceMongoRepository.findByResourceIdInAndStatus(ids, null);
        if (!CollectionUtils.isEmpty(list)) {
           return ResultUtil.params("要"+title+"的资源正在被系统使用,不能被"+title+"");
        }*/
        return ResultUtil.success();
    }
}

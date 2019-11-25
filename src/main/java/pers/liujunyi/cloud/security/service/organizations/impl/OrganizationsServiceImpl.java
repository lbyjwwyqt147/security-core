package pers.liujunyi.cloud.security.service.organizations.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.UserContext;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.repository.mongo.organizations.OrganizationsMongoRepository;
import pers.liujunyi.cloud.security.repository.mongo.organizations.StaffOrgMongoRepository;
import pers.liujunyi.cloud.security.repository.jpa.organizations.OrganizationsRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: OrganizationsServiceImpl.java
 * 文件描述: 组织机构 Service Impl
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
    private OrganizationsMongoRepository organizationsMongoRepository;
    @Autowired
    private StaffOrgMongoRepository staffOrgMongoRepository;

    public OrganizationsServiceImpl(BaseRepository<Organizations, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(OrganizationsDto record) {
        boolean add = record.getId() != null ? true : false;
        if (this.checkOrgNumberRepetition(record.getOrgNumber(), record.getId())) {
            return ResultUtil.params("机构代码重复,请重新输入！");
        }
        Organizations organizations = DozerBeanMapperUtil.copyProperties(record, Organizations.class);
        if (record.getSeq() == null) {
            organizations.setSeq(10);
        }
        if (record.getOrgStatus() == null) {
            organizations.setOrgStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (record.getId() != null) {
            organizations.setUpdateTime(new Date());
            organizations.setUpdateUserId(UserContext.currentUserId());
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
            organizations.setFullParent("0");
        }
        organizations.setFullName(record.getOrgName());
        Organizations saveObject = this.organizationsRepository.save(organizations);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObject.setDataVersion(saveObject.getDataVersion() + 1);
        }
        this.organizationsMongoRepository.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids,String putParams) {
        if (status.byteValue() == 1) {
            List<StaffOrg> list = this.staffOrgMongoRepository.findByOrgIdIn(ids);
            if (!CollectionUtils.isEmpty(list)) {
                ResultUtil.params("要禁用的组织机构正在被系统使用,不能被禁用");
            }
        }
        int count = this.organizationsRepository.setOrgStatusByIds(status, new Date(), ids);
        if (count > 0) {
            JSONArray jsonArray = JSONArray.parseArray(putParams);
            int jsonSize = jsonArray.size();
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            for(int i = 0; i < jsonSize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> docDataMap = new HashMap<>();
                docDataMap.put("orgStatus", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                docDataMap.put("dataVersion", jsonObject.getLongValue("dataVersion") + 1);
                sourceMap.put(jsonObject.getString("id"), docDataMap);
            }
            // 更新 Mongo 中的数据
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();

    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id, Long version) {
        if (status.byteValue() == 1) {
            List<StaffOrg> list = this.staffOrgMongoRepository.findByOrgId(id);
            if (!CollectionUtils.isEmpty(list)) {
                ResultUtil.params("要禁用的组织机构正在被系统使用,不能被禁用");
            }
        }
        int count = this.organizationsRepository.setStatusById(status, new Date(), id, version);
        if (count > 0) {
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("orgStatus", status);
            docDataMap.put("dataVersion", version + 1);
            docDataMap.put("updateTime", System.currentTimeMillis());
            super.updateMongoDataById(id, docDataMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        List<StaffOrg> list = this.staffOrgMongoRepository.findByOrgIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            ResultUtil.params("要删除的组织机构正在被系统使用,不能被删除");
        }
        long count = this.organizationsRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.organizationsMongoRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteSingle(Long id) {
        List<StaffOrg> list = this.staffOrgMongoRepository.findByOrgId(id);
        if (!CollectionUtils.isEmpty(list)) {
            ResultUtil.params("要删除的组织机构正在被系统使用,不能被删除");
        }
        this.organizationsRepository.deleteById(id);
        this.organizationsMongoRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort =  Sort.by(Sort.Direction.ASC, "id");
        List<Organizations> list = this.organizationsRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.organizationsMongoRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<Organizations> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.organizationsMongoRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.organizationsMongoRepository.saveAll(list);
                }
            } else {
                this.organizationsMongoRepository.saveAll(list);
            }
        } else {
            this.organizationsMongoRepository.deleteAll();
        }
        return ResultUtil.success();
    }


    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        Optional<Organizations> organizations = this.organizationsMongoRepository.findById(id);
        if (organizations.isPresent()) {
            return organizations.get();
        }
        return null;
    }


    /**
     * 检测机构代码是否重复
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
        Organizations organizations = this.organizationsMongoRepository.findFirstByOrgNumber(orgNumber);
        if (organizations != null) {
            return true;
        }
        return false;
    }



}

package pers.liujunyi.cloud.security.service.organizations.impl;

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
import pers.liujunyi.cloud.common.util.UserUtils;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.repository.elasticsearch.organizations.OrganizationsElasticsearchRepository;
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
    private OrganizationsElasticsearchRepository organizationsElasticsearchRepository;
    @Autowired
    private UserUtils userUtils;


    public OrganizationsServiceImpl(BaseRepository<Organizations, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(OrganizationsDto record) {
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
            organizations.setUpdateUserId(this.userUtils.getPresentLoginUserId());
        }
        if (record.getParentId().longValue() > 0) {
            Organizations parent = this.getOrganizations(record.getParentId());
            organizations.setFullParent(parent.getFullParent() + ":"  + parent.getId());
            organizations.setOrgLevel((byte)(parent.getOrgLevel() + 1));
            organizations.setFullParentCode(StringUtils.isNotBlank(parent.getFullParentCode()) ? parent.getFullParentCode()   + ":" + parent.getOrgNumber() : parent.getOrgNumber());
        } else {
            organizations.setFullParent("0");
            organizations.setOrgLevel((byte) 1);
            organizations.setFullParent(null);
        }
        organizations.setFullName(record.getOrgName());
        Organizations saveObject = this.organizationsRepository.save(organizations);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        this.organizationsElasticsearchRepository.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.organizationsRepository.setOrgStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("orgStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            // 更新 Elasticsearch 中的数据
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        long count = this.organizationsRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.organizationsElasticsearchRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo singleDelete(Long id) {
        this.organizationsRepository.deleteById(id);
        this.organizationsElasticsearchRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<Organizations> list = this.organizationsRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.organizationsElasticsearchRepository.deleteAll();
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
                    this.organizationsElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.organizationsElasticsearchRepository.saveAll(list);
                }
            } else {
                this.organizationsElasticsearchRepository.saveAll(list);
            }
        } else {
            this.organizationsElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }

    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        Optional<Organizations> organizations = this.organizationsElasticsearchRepository.findById(id);
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
        Organizations organizations = this.organizationsElasticsearchRepository.findFirstByOrgNumber(orgNumber);
        if (organizations != null) {
            return true;
        }
        return false;
    }



}

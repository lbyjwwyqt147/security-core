package pers.liujunyi.cloud.security.service.organizations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.repository.mongo.organizations.StaffOrgMongoRepository;
import pers.liujunyi.cloud.security.repository.jpa.organizations.StaffOrgRepository;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 文件名称: StaffOrgServiceImpl.java
 * 文件描述: 职工关联组织机构 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgServiceImpl extends BaseServiceImpl<StaffOrg, Long> implements StaffOrgService {

    @Autowired
    private StaffOrgRepository staffOrgRepository;
    @Autowired
    private StaffOrgMongoRepository staffOrgMongoRepository;


    public StaffOrgServiceImpl(BaseRepository<StaffOrg, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(StaffOrg org, List<Long> staffIds) {
        List<StaffOrg> list = new LinkedList<>();
        staffIds.stream().forEach(item -> {
            org.setStaffId(item);
            org.setStatus(SecurityConstant.ENABLE_STATUS);
            list.add(org);
        });
        List<StaffOrg> saveObj = this.staffOrgRepository.saveAll(list);
        if (!CollectionUtils.isEmpty(saveObj)) {
            this.staffOrgMongoRepository.saveAll(saveObj);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.staffOrgRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            // 更新 Mongo 中的数据
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public int updateStatusByStaffIds(Byte status, List<Long> staffIds) {
        Map<String, Object> updateParams = new ConcurrentHashMap<>();
        updateParams.put("status", status);
        AtomicInteger count = new AtomicInteger(0);
        staffIds.stream().forEach(item -> {
            Map<String, Object> queryParams = new ConcurrentHashMap<>();
            queryParams.put("staffId", item);
            boolean success = super.updateMongoData(queryParams, updateParams);
            if (success) {
                count.getAndSet(1);
            }
        });
        return count.get();
    }

    @Override
    public int updateStatusByOrgIds(Byte status, List<Long> orgIds) {
        Map<String, Object> updateParams = new ConcurrentHashMap<>();
        updateParams.put("status", status);
        AtomicInteger count = new AtomicInteger(0);
        orgIds.stream().forEach(item -> {
            Map<String, Object> queryParams = new ConcurrentHashMap<>();
            queryParams.put("orgId", item);
            boolean success = super.updateMongoData(queryParams, updateParams);
            if (success) {
                count.getAndSet(1);
            }
        });
        return count.get();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        long count = this.staffOrgRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.staffOrgMongoRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteSingle(Long id) {
        this.staffOrgRepository.deleteById(id);
        this.staffOrgMongoRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public long deleteByOrgId(Long orgId) {
        long count = this.staffOrgRepository.deleteByOrgId(orgId);
        if (count > 0) {
            this.staffOrgMongoRepository.deleteByOrgId(orgId);
        }
        return count;
    }

    @Override
    public long deleteByOrgIds(List<Long> orgIds) {
        long count = this.staffOrgRepository.deleteByOrgIdIn(orgIds);
        if (count > 0) {
            this.staffOrgMongoRepository.deleteByOrgIdIn(orgIds);
        }
        return count;
    }

    @Override
    public long deleteByStaffId(Long staffId) {
        long count = this.staffOrgRepository.deleteByStaffId(staffId);
        if (count > 0) {
            this.staffOrgMongoRepository.deleteByStaffId(staffId);
        }
        return count;
    }

    @Override
    public long deleteByStaffIds(List<Long> staffIds) {
        long count = this.staffOrgRepository.deleteByStaffIdIn(staffIds);
        if (count > 0) {
            this.staffOrgMongoRepository.deleteByStaffIdIn(staffIds);
        }
        return count;
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort =  Sort.by(Sort.Direction.ASC, "id");
        List<StaffOrg> list = this.staffOrgRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.staffOrgMongoRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<StaffOrg> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.staffOrgMongoRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.staffOrgMongoRepository.saveAll(list);
                }
            } else {
                this.staffOrgMongoRepository.saveAll(list);
            }
        } else {
            this.staffOrgMongoRepository.deleteAll();
        }
        return ResultUtil.success();
    }
}

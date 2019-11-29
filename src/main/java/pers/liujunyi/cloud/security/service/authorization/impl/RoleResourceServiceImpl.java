package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.repository.jpa.authorization.RoleResourceRepository;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 文件名称: RoleResourceServiceImpl.java
 * 文件描述: 角色资源 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleResourceServiceImpl  extends BaseServiceImpl<RoleResource, Long> implements RoleResourceService {

    @Autowired
    private RoleResourceRepository roleResourceRepository;
    @Autowired
    private RoleResourceMongoRepository roleResourceMongoRepository;

    public RoleResourceServiceImpl(BaseRepository<RoleResource, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(RoleResource resource, List<Long> resourceIds) {
        List<RoleResource> list = new LinkedList<>();
        resourceIds.stream().forEach(item -> {
            resource.setResourceId(item);
            resource.setStatus(SecurityConstant.ENABLE_STATUS);
            list.add(resource);
        });
        List<RoleResource> saveObj = this.roleResourceRepository.saveAll(list);
        if (!CollectionUtils.isEmpty(saveObj)) {
            this.roleResourceMongoRepository.saveAll(saveObj);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.roleResourceRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        long count = this.roleResourceRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.roleResourceMongoRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public int setStatusByRoleIds(Byte status, List<Long> roleIds) {
        Map<String, Object> updateParams = new ConcurrentHashMap<>();
        updateParams.put("status", status);
        AtomicInteger count = new AtomicInteger(0);
        roleIds.stream().forEach(item -> {
            Map<String, Object> queryParams = new ConcurrentHashMap<>();
            queryParams.put("roleId", item);
            boolean success = super.updateMongoData(queryParams, updateParams);
            if (success) {
                count.getAndSet(1);
            }
        });
        return count.get();
    }

    @Override
    public int setStatusByResourceIds(Byte status, List<Long> resourceIds) {
        Map<String, Object> updateParams = new ConcurrentHashMap<>();
        updateParams.put("status", status);
        AtomicInteger count = new AtomicInteger(0);
        resourceIds.stream().forEach(item -> {
            Map<String, Object> queryParams = new ConcurrentHashMap<>();
            queryParams.put("resourceId", item);
            boolean success = super.updateMongoData(queryParams, updateParams);
            if (success) {
                count.getAndSet(1);
            }
        });
        return count.get();
    }

    @Override
    public long deleteByRoleIdIn(List<Long> roleIds) {
        long count = this.roleResourceRepository.deleteByRoleIdIn(roleIds);
        if (count > 0) {
            this.roleResourceMongoRepository.deleteByRoleIdIn(roleIds);
        }
        return count;
    }

    @Override
    public long deleteByResourceIdIn(List<Long> resourceIds) {
        long count = this.roleResourceRepository.deleteByResourceIdIn(resourceIds);
        if (count > 0) {
            this.roleResourceMongoRepository.deleteByResourceIdIn(resourceIds);
        }
        return count;
    }

    @Override
    public ResultInfo syncDataToMongo() {
        super.syncDataMongoDb();
        return ResultUtil.success();
    }
}

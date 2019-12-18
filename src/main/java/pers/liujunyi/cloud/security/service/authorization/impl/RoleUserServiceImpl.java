package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseJpaRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseJpaMongoServiceImpl;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.repository.jpa.authorization.RoleUserRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleUserService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 文件名称: RoleUserServiceImpl.java
 * 文件描述: 人员角色 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleUserServiceImpl  extends BaseJpaMongoServiceImpl<RoleUser, Long> implements RoleUserService {

    @Autowired
    private RoleUserRepository roleUserRepository;
    @Autowired
    private RoleUserMongoService roleUserMongoService;

    public RoleUserServiceImpl(BaseJpaRepository<RoleUser, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(String userIds, List<Long> roleIds) {
        List<RoleUser> list = new LinkedList<>();
        List<Long> userIdList = SystemUtils.idToLong(userIds);
       // this.deleteByUserIdIn(userIdList);
        userIdList.stream().forEach(userId -> {
            roleIds.stream().forEach(item -> {
                RoleUser roleUser = new RoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(item);
                roleUser.setStatus(SecurityConstant.ENABLE_STATUS);
                list.add(roleUser);
            });
        });

        List<RoleUser> saveObj = this.roleUserRepository.saveAll(list);
        if (!CollectionUtils.isEmpty(saveObj)) {
            this.roleUserMongoService.saveAll(saveObj);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.roleUserRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<Long, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("status", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(item, docDataMap);
            });
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(Long userId, List<Long> roleIds) {
        long count = this.roleUserRepository.deleteByUserIdAndRoleIdIn(userId, roleIds);
        if (count > 0) {
            this.roleUserMongoService.deleteByUserIdAndRoleIdIn(userId, roleIds);
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
    public int setStatusByUserIds(Byte status, List<Long> userIds) {
        Map<String, Object> updateParams = new ConcurrentHashMap<>();
        updateParams.put("status", status);
        AtomicInteger count = new AtomicInteger(0);
        userIds.stream().forEach(item -> {
            Map<String, Object> queryParams = new ConcurrentHashMap<>();
            queryParams.put("userId", item);
            boolean success = super.updateMongoData(queryParams, updateParams);
            if (success) {
                count.getAndSet(1);
            }
        });
        return count.get();
    }

    @Override
    public long deleteByRoleIdIn(List<Long> roleIds) {
        long count = this.roleUserRepository.deleteByRoleIdIn(roleIds);
        if (count > 0) {
            this.roleUserMongoService.deleteByRoleIdIn(roleIds);
        }
        return count;
    }

    @Override
    public long deleteByUserIdIn(List<Long> userIds) {
        long count = this.roleUserRepository.deleteByUserIdIn(userIds);
        if (count > 0) {
            this.roleUserMongoService.deleteByUserIdIn(userIds);
        }
        return count;
    }

    @Override
    public ResultInfo syncDataToMongo() {
        super.syncDataMongoDb();
        return ResultUtil.success();
    }
}

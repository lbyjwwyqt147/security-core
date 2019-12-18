package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleInfoMongoRepository;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleUserMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 文件名称: RoleUserMongoServiceImpl.java
 * 文件描述: 人员角色 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleUserMongoServiceImpl extends BaseMongoServiceImpl<RoleUser, Long> implements RoleUserMongoService {

    @Autowired
    private RoleUserMongoRepository roleUserMongoRepository;
    @Autowired
    private RoleInfoMongoRepository roleInfoMongoRepository;

    public RoleUserMongoServiceImpl(BaseMongoRepository<RoleUser, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<RoleUser> findByUserIdAndStatus(Long userId, Byte status) {
        return this.roleUserMongoRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<RoleUser> findByUserIdInAndStatus(List<Long> userIds, Byte status) {
        return this.roleUserMongoRepository.findByUserIdInAndStatus(userIds, status);
    }

    @Override
    public ResultInfo findPageGird(RoleUser query) {
        List<RoleInfo> roleInfoList = new LinkedList<>();
        List<RoleUser> roleUsers = this.findByUserIdAndStatus(query.getUserId(), SecurityConstant.ENABLE_STATUS);
        if (!CollectionUtils.isEmpty(roleUsers)) {
            List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
            if (query.getRoleId() == null) {
                roleInfoList = this.roleInfoMongoRepository.findAllByIdIn(roleIds);
            } else {
                roleInfoList = this.roleInfoMongoRepository.findByIdNotIn(roleIds);
            }
        } else {
            if (query.getRoleId() != null) {
                roleInfoList = this.roleInfoMongoRepository.findByRoleStatus(SecurityConstant.ENABLE_STATUS);
            }
        }
        ResultInfo result = ResultUtil.success(roleInfoList, super.secretKey);
        result.setTotal((long)roleInfoList.size());
        return  result;
    }

    @Override
    public List<RoleUser> findByRoleIdAndStatus(Long roleId, Byte status) {
        return this.roleUserMongoRepository.findByRoleIdAndStatus(roleId, status);
    }

    @Override
    public List<RoleUser> findByRoleIdInAndStatus(List<Long> roleIds, Byte status) {
        return this.roleUserMongoRepository.findByRoleIdInAndStatus(roleIds, status);
    }

    @Override
    public List<RoleInfo> getRoleInfoByUserId(Long userId) {
        List<RoleUser> roleUsers = this.findByUserIdAndStatus(userId, SecurityConstant.ENABLE_STATUS);
        if (!CollectionUtils.isEmpty(roleUsers)) {
            List<Long> roleIds = roleUsers.stream().map(RoleUser::getRoleId).distinct().collect(Collectors.toList());
            return this.roleInfoMongoRepository.findAllByIdIn(roleIds);
        }
        return null;
    }

    @Transactional(
            value = "mongoTransactionManager",
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public long deleteByRoleIdIn(List<Long> roleIds) {
        return this.roleUserMongoRepository.deleteByRoleIdIn(roleIds);
    }

    @Transactional(
            value = "mongoTransactionManager",
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public long deleteByUserIdIn(List<Long> userIds) {
        return  this.roleUserMongoRepository.deleteByUserIdIn(userIds);
    }

    @Transactional(
            value = "mongoTransactionManager",
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public long deleteByUserIdAndRoleIdIn(Long userId, List<Long> roleIds) {
        return this.roleUserMongoRepository.deleteByUserIdAndRoleIdIn(userId, roleIds);
    }

}

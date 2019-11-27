package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleUserMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;

import java.util.List;
import java.util.Map;

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

    public RoleUserMongoServiceImpl(BaseMongoRepository<RoleUser, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<RoleUser> findByUserIdAndStatus(Long userId, Byte status) {
        return null;
    }

    @Override
    public List<RoleUser> findByUserIdIn(List<Long> userIds) {
        return null;
    }

    @Override
    public List<RoleUser> findByUserIdInOrderByIdAsc(List<Long> userIds) {
        return null;
    }

    @Override
    public ResultInfo findPageGird(RoleUser query) {
        return null;
    }

    @Override
    public List<RoleUser> findByRoleIdAndStatus(Long roleId, Byte status) {
        return null;
    }

    @Override
    public List<RoleUser> findByRoleIdIn(List<Long> roleIds) {
        return null;
    }

    @Override
    public List<RoleUser> getUserInfoByRoleId(Long roleId) {
        return null;
    }

    @Override
    public Map<Long, List<RoleUser>> getUserInfoByRoleIdIn(List<Long> roleId) {
        return null;
    }

    @Override
    public List<RoleUser> getRoleInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public Map<Long, List<RoleUser>> getRoleInfoByUserIdIn(List<Long> userIds) {
        return null;
    }
}

package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.repository.jpa.authorization.RoleUserRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleUserService;

import java.util.List;

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
public class RoleUserServiceImpl  extends BaseServiceImpl<RoleUser, Long> implements RoleUserService {

    @Autowired
    private RoleUserRepository roleUserRepository;

    public RoleUserServiceImpl(BaseRepository<RoleUser, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(RoleUser user, List<Long> roleIds) {
        return null;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        return null;
    }

    @Override
    public int setStatusByRoleIds(Byte status, List<Long> roleIds) {
        return 0;
    }

    @Override
    public int setStatusByUserIds(Byte status, List<Long> userIds) {
        return 0;
    }

    @Override
    public long deleteByRoleId(Long roleId) {
        return 0;
    }

    @Override
    public long deleteByRoleIdIn(List<Long> roleIds) {
        return 0;
    }

    @Override
    public long deleteByUserIdIn(List<Long> userIds) {
        return 0;
    }

    @Override
    public long deleteByUserId(Long userId) {
        return 0;
    }

    @Override
    public ResultInfo syncDataToMongo() {
        return null;
    }
}

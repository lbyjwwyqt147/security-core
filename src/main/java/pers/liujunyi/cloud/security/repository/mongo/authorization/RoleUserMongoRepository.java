package pers.liujunyi.cloud.security.repository.mongo.authorization;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;

import java.util.List;

/***
 * 文件名称: RoleUserMongoRepository.java
 * 文件描述: 人员角色 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleUserMongoRepository extends BaseMongoRepository<RoleUser, Long> {

    /**
     * 根据 用户id 获取数据
     * @param userIds
     * @param status
     * @return
     */
    List<RoleUser> findByUserIdInAndStatus(List<Long> userIds, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleIds
     * @param status
     * @return
     */
    List<RoleUser> findByRoleIdInAndStatus(List<Long> roleIds, Byte status);



    /**
     * 根据 用户id 获取数据
     * @param userId
     * @param  status
     * @return
     */
    List<RoleUser> findByUserIdAndStatus(Long userId, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleId
     * @param  status
     * @return
     */
    List<RoleUser> findByRoleIdAndStatus(Long roleId, Byte status);

    /**
     * 根据 roleId 删除
     * @param roleId 角色id
     * @return
     */
    long deleteByRoleId(Long roleId);

    /**
     * 根据 roleId 删除
     * @param roleIds 角色ID
     * @return
     */
    long deleteByRoleIdIn(List<Long> roleIds);

    /**
     * 根据 userId 删除
     * @param userIds 用户id
     * @return
     */
    long deleteByUserIdIn(List<Long> userIds);

    /**
     * 根据 userId 删除
     * @param userId 用户id
     * @return
     */
    long deleteByUserId(Long userId);
    
}

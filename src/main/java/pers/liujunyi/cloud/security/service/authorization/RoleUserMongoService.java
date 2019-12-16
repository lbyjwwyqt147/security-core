package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;

import java.util.List;

/***
 * 文件名称: RoleUserMongoService.java
 * 文件描述: 人员角色 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleUserMongoService extends BaseMongoService<RoleUser, Long> {

    /**
     * 根据 用户id 获取数据
     * @param userId
     * @param  status
     * @return
     */
    List<RoleUser> findByUserIdAndStatus(Long userId, Byte status);

    /**
     * 根据 用户id 获取数据
     * @param userIds
     * @return
     */
    List<RoleUser> findByUserIdInAndStatus(List<Long> userIds, Byte status);


    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(RoleUser query);

    /**
     * 根据 角色id 获取数据
     * @param roleId
     * @param status
     * @return
     */
    List<RoleUser> findByRoleIdAndStatus(Long roleId, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleIds
     * @return
     */
    List<RoleUser> findByRoleIdInAndStatus(List<Long> roleIds, Byte status);


    /**
     * 根据用户ID 获取所属角色信息
     * @param userId
     * @return 返回  用户信息
     */
    List<RoleInfo> getRoleInfoByUserId(Long userId);

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
     * 根据 userId roleIds 删除
     * @param userId 用户id
     * @param roleIds 角色ID
     * @return
     */
    long deleteByUserIdAndRoleIdIn(Long userId, List<Long> roleIds);
}

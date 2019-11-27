package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;

import java.util.List;
import java.util.Map;

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
    List<RoleUser> findByUserIdIn(List<Long> userIds);

    /**
     * 根据 用户id 获取数据
     * @param userIds
     * @return
     */
    List<RoleUser> findByUserIdInOrderByIdAsc(List<Long> userIds);

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
    List<RoleUser> findByRoleIdIn(List<Long> roleIds);


    /**
     * 根据角色ID 获取所属用户信息
     * @param roleId
     * @return 返回  用户信息
     */
    List<RoleUser> getUserInfoByRoleId(Long roleId);

    /**
     * 根据角色ID 获取所属用户信息
     * @param roleId
     * @return 返回  map  key = roleId  valud  = 用户信息
     */
    Map<Long, List<RoleUser>> getUserInfoByRoleIdIn(List<Long> roleId);

    /**
     * 根据用户ID 获取所属角色信息
     * @param userId
     * @return 返回  用户信息
     */
    List<RoleUser> getRoleInfoByUserId(Long userId);

    /**
     * 根据用户ID 获取所属角色信息
     * @param userIds
     * @return 返回  map  key = roleId  valud  = 角色信息
     */
    Map<Long, List<RoleUser>> getRoleInfoByUserIdIn(List<Long> userIds);
}

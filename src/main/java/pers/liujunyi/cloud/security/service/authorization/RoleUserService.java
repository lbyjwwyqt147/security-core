package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;

import java.util.List;

/***
 * 文件名称: RoleUserService.java
 * 文件描述:  人员角色 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleUserService  extends BaseService<RoleUser, Long> {

    /**
     * 保存数据
     * @param user  资源
     * @param roleIds 角色ID
     * @return
     */
    ResultInfo saveRecord(RoleUser user, List<Long> roleIds);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids);

    /**
     * 根据 roleId 修改状态
     * @param status  0：正常  1：禁用
     * @param roleIds 角色ID
     * @return
     */
    int setStatusByRoleIds(Byte status, List<Long> roleIds);

    /**
     * 根据 userId 修改状态
     * @param status  0：正常  1：禁用
     * @param userIds 用户id
     * @return
     */
    int setStatusByUserIds(Byte status, List<Long> userIds);

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
    /**
     * 同步数据到Mongo中
     * @return
     */
    ResultInfo syncDataToMongo();

}

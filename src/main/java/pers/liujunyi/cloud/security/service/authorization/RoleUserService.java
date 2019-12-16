package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseJpaMongoService;
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
public interface RoleUserService  extends BaseJpaMongoService<RoleUser, Long> {

    /**
     * 保存数据
     * @param userIds  人员
     * @param roleIds 角色ID
     * @return
     */
    ResultInfo saveRecord(String userIds, List<Long> roleIds);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);

    /**
     * 批量删除
     * @param userId
     * @param roleIds
     * @return
     */
    ResultInfo deleteBatch(Long userId, List<Long> roleIds);

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
     * 同步数据到Mongo中
     * @return
     */
    ResultInfo syncDataToMongo();

}

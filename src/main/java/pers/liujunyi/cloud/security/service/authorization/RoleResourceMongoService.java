package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;

import java.util.List;

/***
 * 文件名称: RoleResourceMongoService.java
 * 文件描述: 角色资源 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleResourceMongoService extends BaseMongoService<RoleResource, Long> {

    /**
     * 根据 资源id 获取数据
     * @param resourceId
     * @param  status
     * @return
     */
    List<RoleResource> findByResourceIdAndStatus(Long resourceId, Byte status);

    /**
     * 根据 资源id 获取数据
     * @param resourceIds
     * @param  status
     * @return
     */
    List<RoleResource> findByResourceIdInAndStatus(List<Long> resourceIds, Byte status);


    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(RoleResource query);

    /**
     * 根据 角色id 获取数据
     * @param roleId
     * @param status
     * @return
     */
    List<RoleResource> findByRoleIdAndStatus(Long roleId, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleIds
     * @param  status
     * @return
     */
    List<RoleResource> findByRoleIdInAndStatus(List<Long> roleIds, Byte status);


    /**
     * 根据角色ID 获取所属资源信息
     * @param roleId
     * @return 返回  资源信息
     */
    List<MenuResource> getResourceInfoByRoleId(Long roleId);


    /**
     * 根据角色ID 获取所属资源信息
     * @param roleId
     * @return 返回 资源信息
     */
    List<MenuResource> getResourceInfoByRoleIdIn(List<Long> roleId);

}

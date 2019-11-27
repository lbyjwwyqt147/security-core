package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;

import java.util.List;
import java.util.Map;

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
     * @return
     */
    List<RoleResource> findByResourceIdIn(List<Long> resourceIds);

    /**
     * 根据 资源id 获取数据
     * @param resourceIds
     * @return
     */
    List<RoleResource> findByResourceIdInOrderByIdAsc(List<Long> resourceIds);

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
     * @return
     */
    List<RoleResource> findByRoleIdIn(List<Long> roleIds);


    /**
     * 根据角色ID 获取所属资源信息
     * @param roleId
     * @return 返回  资源信息
     */
    List<RoleResource> getResourceInfoByRoleId(Long roleId);

    /**
     * 根据角色ID 获取所属资源信息
     * @param roleId
     * @return 返回  map  key = roleId  valud  = 资源信息
     */
    Map<Long, List<RoleResource>> getResourceInfoByRoleIdIn(List<Long> roleId);

    /**
     * 根据资源ID 获取所属角色信息
     * @param resourceId
     * @return 返回  资源信息
     */
    List<RoleResource> getResourceInfoByResourceId(Long resourceId);

    /**
     * 根据资源ID 获取所属角色信息
     * @param resourceIds
     * @return 返回  map  key = roleId  valud  = 角色信息
     */
    Map<Long, List<RoleResource>> getResourceInfoByResourceIdIn(List<Long> resourceIds);

}

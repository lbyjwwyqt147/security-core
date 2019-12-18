package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
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
     * @param  status
     * @return
     */
    List<RoleResource> findByResourceIdInAndStatus(List<Long> resourceIds, Byte status);

    /**
     *  根据 资源pid 符合 ztree 结构的数据
     * @param roleId 角色ID
     * @param resourcePid  资源pid
     * @return
     */
    List<ZtreeNode> resourceSelectedTree(Long roleId, Long resourcePid);


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
    List<MenuResource> getResourceInfoByRoleIdIn(List<Long> roleId, Byte menuClassify);

    /**
     * 根据角色ID 获取所属资源信息
     * @param roleId
     * @return 返回 资源信息
     */
    Map<Long, List<MenuResource>> getResourceByRoleIdIn(List<Long> roleId, Byte menuClassify);


    /**
     * 根据 roleId 删除
     * @param roleIds 角色ID
     * @return
     */
    long deleteByRoleIdIn(List<Long> roleIds);

    /**
     * 根据 resourceId 删除
     * @param resourceIds 资源id
     * @return
     */
    long deleteByResourceIdIn(List<Long> resourceIds);

    /**
     * 获取用户所分配的资源菜单
     * @param userId
     * @return
     */
    ResultInfo getUserResourceMenu(Long userId);

    /**
     * 获取用户所分配的资源菜单
     * @param userId
     * @return
     */
    List<MenuResource> getUserResourceFunction(Long userId);
}

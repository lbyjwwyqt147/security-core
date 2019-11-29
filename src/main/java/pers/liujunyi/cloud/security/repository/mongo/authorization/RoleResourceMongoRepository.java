package pers.liujunyi.cloud.security.repository.mongo.authorization;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;

import java.util.List;

/***
 * 文件名称: RoleResourceMongoRepository.java
 * 文件描述: 角色资源 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleResourceMongoRepository extends BaseMongoRepository<RoleResource, Long> {


    /**
     * 根据 资源id 获取数据
     * @param resourceIds
     * @param status
     * @return
     */
    List<RoleResource> findByResourceIdInAndStatus(List<Long> resourceIds, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleIds
     * @param status
     * @return
     */
    List<RoleResource> findByRoleIdInAndStatus(List<Long> roleIds, Byte status);


    /**
     * 根据 资源id 获取数据
     * @param resourceId
     * @param  status
     * @return
     */
    List<RoleResource> findByResourceIdAndStatus(Long resourceId, Byte status);

    /**
     * 根据 角色id 获取数据
     * @param roleId
     * @param  status
     * @return
     */
    List<RoleResource> findByRoleIdAndStatus(Long roleId, Byte status);


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

}

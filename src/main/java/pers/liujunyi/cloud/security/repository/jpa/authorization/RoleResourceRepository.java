package pers.liujunyi.cloud.security.repository.jpa.authorization;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: RoleResourceRepository.java
 * 文件描述: 角色资源 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface RoleResourceRepository extends BaseRepository<RoleResource, Long> {

    /**
     * 根据 id 修改状态
     * @param status  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update role_resource u set u.status = ?1, u.update_time = ?2 where u.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte status, Date updateTime, List<Long> ids);


    /**
     * 根据 roleId 修改状态
     * @param status  0：正常  1：禁用
     * @param roleIds 角色ID
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update role_resource u set u.status = ?1, u.update_time = ?2, where u.role_id in (?3)", nativeQuery = true)
    int setStatusByRoleIds(Byte status, Date updateTime, List<Long> roleIds);

    /**
     * 根据 resourceId 修改状态
     * @param status  0：正常  1：禁用
     * @param resourceIds 资源 id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update role_resource u set u.status = ?1, u.update_time = ?2, where u.resource_id = ?3", nativeQuery = true)
    int setStatusByResourceIds(Byte status, Date updateTime, List<Long> resourceIds);

    /**
     * 根据 roleId 删除
     * @param roleId 角色id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByRoleId(Long roleId);

    /**
     * 根据 roleId 删除
     * @param roleIds 角色ID
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByRoleIdIn(List<Long> roleIds);

    /**
     * 根据 resourceId 删除
     * @param resourceIds 资源id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByResourceIdIn(List<Long> resourceIds);

    /**
     * 根据 resourceId 删除
     * @param resourceId 资源id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByResourceId(Long resourceId);
}

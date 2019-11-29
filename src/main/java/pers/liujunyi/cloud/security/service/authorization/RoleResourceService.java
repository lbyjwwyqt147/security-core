package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;

import java.util.List;

/***
 * 文件名称: RoleResourceService.java
 * 文件描述:  角色资源 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleResourceService  extends BaseService<RoleResource, Long> {

    /**
     * 保存数据
     * @param resource  资源
     * @param resourceIds 资源ID
     * @return
     */
    ResultInfo saveRecord(RoleResource resource, List<Long> resourceIds);

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
     * 根据 resourceId 修改状态
     * @param status  0：正常  1：禁用
     * @param resourceIds 资源 id
     * @return
     */
    int setStatusByResourceIds(Byte status, List<Long> resourceIds);

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
     * 同步数据到Mongo中
     * @return
     */
    ResultInfo syncDataToMongo();

}

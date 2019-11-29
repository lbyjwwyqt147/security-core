package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.RoleInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: RoleInfoMongoService.java
 * 文件描述: 角色信息 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleInfoMongoService extends BaseMongoService<RoleInfo, Long> {

    /**
     *  根据 pid 符合 ztree 结构的数据
     * @param pid
     * @param status
     * @return
     */
    List<ZtreeNode> roleTree(Long pid, Byte status);

    /**
     * 根据 fullRoleParentCode 获取 符合 ztree 结构的数据
     * @param fullParentCode
     * @return
     */
    List<ZtreeNode> roleFullParentCodeTree(String fullParentCode);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(RoleInfoQueryDto query);

    /**
     * 根据ID 获取角色名称
     * @param id
     * @return
     */
    String getRoleName(Long id);

    /**
     * 根据ID获取详细信息
     * @param id
     * @return
     */
    ResultInfo selectById(Long id);

    /**
     * 根据 ID  获取 角色名称 返回 map
     * @param ids
     * @return 返回  map  key = id  valud  = 角色名称
     */
    Map<Long, String> roleNameToMap(List<Long> ids);

    /**
     * 根据ID获取信息
     * @param id
     * @return
     */
    RoleInfo findById(Long id);
    
}

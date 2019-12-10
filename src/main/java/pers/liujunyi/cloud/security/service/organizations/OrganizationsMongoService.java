package pers.liujunyi.cloud.security.service.organizations;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: OrganizationsMongoService.java
 * 文件描述: 组织机构信息 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsMongoService extends BaseMongoService<Organizations, Long> {

    /**
     * 根据 机构编号 获取数据
     * @param orgNumber
     * @return
     */
    Organizations findFirstByOrgNumber(String orgNumber);

    /**
     *  根据 pid 符合 ztree 结构的数据
     * @param pid
     * @param status
     * @return
     */
    List<ZtreeNode> orgTree(Long pid, Byte status);

    /**
     * 根据 fullParentCode 获取 符合 ztree 结构的数据
     * @param fullParentCode
     * @return
     */
    List<ZtreeNode> orgFullParentCodeTree(String fullParentCode);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(OrganizationsQueryDto query);

    /**
     * 根据ID获取机构全名称
     * @param id
     * @return
     */
    String getOrgFullName(Long id);

    /**
     * 根据ID 获取机构名称
     * @param id
     * @return
     */
    String getOrgName(Long id);

    /**
     * 根据一组id 获取数据
     * @param ids
     * @return key = id  value = name
     */
    Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids);

    /**
     * 根据ID获取详细信息
     * @param id
     * @return
     */
    ResultInfo selectById(Long id);


    /**
     * 根据 ID  获取 组织机构全名称
     * @param id
     * @return
     */
    String getFullOrgName(Long id);


    /**
     * 根据 ID  获取 组织机构全名称 返回 map
     * @param ids
     * @return 返回  map  key = id  valud  = 机构全名称
     */
    Map<Long, String> fullOrgNameToMap(List<Long> ids);

    /**
     * 根据 ID  获取 组织机构名称 返回 map
     * @param ids
     * @return 返回  map  key = id  valud  = 机构名称
     */
    Map<Long, String> orgNameToMap(List<Long> ids);
}

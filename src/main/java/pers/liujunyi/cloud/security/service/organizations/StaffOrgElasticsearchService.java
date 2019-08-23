package pers.liujunyi.cloud.security.service.organizations;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.List;
import java.util.Map;


/***
 * 文件名称: StaffOrgElasticsearchService.java
 * 文件描述: 职工关联组织机构信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgElasticsearchService extends BaseElasticsearchService<StaffOrg, Long> {


    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @param  status
     * @return
     */
    List<StaffOrg> findByOrgIdAndStatus(Long orgId, Byte status);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @return
     */
    List<StaffOrg> findByOrgIdIn(List<Long> orgIds);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @return
     */
    List<StaffOrg> findByOrgIdInOrderByIdAsc(List<Long> orgIds);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(StaffOrgQueryDto query);

    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @param status
     * @return
     */
    List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status);

    /**
     * 根据 职工id 获取数据
     * @param staffIds
     * @return
     */
    List<StaffOrg> findByStaffIdIn(List<Long> staffIds);

    /**
     * 根据员工ID  获取 组织机构全名称
     * @param staffId
     * @return
     */
    String getFullOrgName(Long staffId);

    /**
     * 根据员工ID  获取 组织机构名称
     * @param staffId
     * @return
     */
    String getOrgName(Long staffId);

    /**
     * 根据员工ID  获取 组织机构全名称 返回 map
     * @param staffId
     * @return 返回  map  key = staffId  valud  = 机构全名称
     */
    Map<Long, String> fullOrgNameToMap(List<Long> staffId);

    /**
     * 根据员工ID  获取 组织机构名称 返回 map
     * @param staffId
     * @return 返回  map  key = staffId  valud  = 机构名称
     */
    Map<Long, String> orgNameToMap(List<Long> staffId);

    /**
     * 根据员工ID 获取所属组织机构信息
     * @param staffId
     * @return 返回  机构信息
     */
    List<Organizations> getOrgInfoByStaffId(Long staffId);

    /**
     * 根据员工ID 获取所属组织机构信息
     * @param staffId
     * @return 返回  map  key = staffId  valud  = 机构信息
     */
    Map<Long, List<Organizations>> getOrgInfoByStaffIdIn(List<Long> staffId);
}

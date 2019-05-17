package pers.liujunyi.cloud.security.repository.elasticsearch.organizations;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.List;

/***
 * 文件名称: StaffOrgElasticsearchRepository.java
 * 文件描述: 职工关联组织机构 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgElasticsearchRepository extends BaseElasticsearchRepository<StaffOrg, Long> {


    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @param  page
     * @return
     */
    List<StaffOrg> findByOrgId(Long orgId, Pageable page);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @param  page
     * @return
     */
    List<StaffOrg> findByOrgIdInOrderByIdAsc(List<Long> orgIds, Pageable page);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @param  page
     * @return
     */
    List<StaffOrg> findByOrgIdIn(List<Long> orgIds, Pageable page);

    /**
     * 根据 职工id 获取数据
     * @param staffIds
     * @param  page
     * @return
     */
    List<StaffOrg> findByStaffIdIn(List<Long> staffIds, Pageable page);


    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @param  page
     * @return
     */
    List<StaffOrg> findByStaffId(Long staffId, Pageable page);

    /**
     * 根据 职工id 获取数据
     * @param staffIds
     * @param  page
     * @return
     */
    List<StaffOrg> findByStaffIdInOrderByIdAsc(List<Long> staffIds, Pageable page);


    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @param  status
     * @param  page
     * @return
     */
    List<StaffOrg> findByOrgIdAndStatus(Long orgId, Byte status, Pageable page);

    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @param  status
     * @param  page
     * @return
     */
    List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status, Pageable page);

    /**
     * 根据 staffId 删除
     * @param staffId 职工id
     * @return
     */
    long deleteByStaffId(Long staffId);

    /**
     * 根据 staffId 删除
     * @param staffIds 职工id
     * @return
     */
    long deleteByStaffIdIn(List<Long> staffIds);

    /**
     * 根据 orgId 删除
     * @param orgIds 组织机构id
     * @return
     */
    long deleteByOrgIdIn(List<Long> orgIds);

    /**
     * 根据 orgId 删除
     * @param orgId 机构id
     * @return
     */
    long deleteByOrgId(Long orgId);
}

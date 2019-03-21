package pers.liujunyi.cloud.security.repository.elasticsearch.organizations;

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
     * @return
     */
    List<StaffOrg> findAllByOrgId(Long orgId);

    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @return
     */
    List<StaffOrg> findAllByStaffId(Long staffId);

    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @param  status
     * @return
     */
    List<StaffOrg> findAllByOrgIdAndStatus(Long orgId, Byte status);

    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @param  status
     * @return
     */
    List<StaffOrg> findAllByStaffIdAndStatus(Long staffId, Byte status);
}

package pers.liujunyi.cloud.security.service.organizations;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsQueryDto;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.List;


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
     * 根据 职工id 获取数据
     * @param staffId
     * @param  status
     * @return
     */
    List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(StaffOrgQueryDto query);


}

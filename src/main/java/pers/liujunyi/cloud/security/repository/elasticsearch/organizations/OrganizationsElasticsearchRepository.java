package pers.liujunyi.cloud.security.repository.elasticsearch.organizations;

import org.springframework.data.domain.Pageable;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;


import java.util.List;

/***
 * 文件名称: OrganizationsElasticsearchRepository.java
 * 文件描述: 组织机构 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsElasticsearchRepository extends BaseElasticsearchRepository<Organizations, Long> {


    /**
     * 根据 机构编号 获取数据
     * @param orgNumber
     * @return
     */
    Organizations findFirstByOrgNumber(String orgNumber);

    /**
     * 根据pid 获取数据
     * @param pid
     * @param orgStatus  0：正常  1：禁用
     * @return
     */
    List<Organizations> findByParentIdAndOrgStatusOrderBySeqAsc(Long pid, Byte orgStatus, Pageable page);

    /**
     * 根据 fullParentCode 获取数据
     * @param fullParentCode
     * @param orgStatus  0：正常  1：禁用
     * @return
     */
    List<Organizations> findByFullParentCodeLikeAndOrgStatusOrderBySeqAsc(String fullParentCode, Byte orgStatus, Pageable page);

}

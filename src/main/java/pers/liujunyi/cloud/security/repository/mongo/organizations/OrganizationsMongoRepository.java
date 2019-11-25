package pers.liujunyi.cloud.security.repository.mongo.organizations;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;

import java.util.List;

/***
 * 文件名称: OrganizationsMongoRepository.java
 * 文件描述: 组织机构 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsMongoRepository extends BaseMongoRepository<Organizations, Long> {


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
    List<Organizations> findByParentIdAndOrgStatusOrderBySeqAsc(Long pid, Byte orgStatus);

    /**
     * 根据 fullParentCode 获取数据
     * @param fullParentCode
     * @param orgStatus  0：正常  1：禁用
     * @return
     */
    List<Organizations> findByFullParentCodeLikeAndOrgStatusOrderBySeqAsc(String fullParentCode, Byte orgStatus);

}

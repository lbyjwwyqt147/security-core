package pers.liujunyi.cloud.security.repository.jpa.organizations;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: OrganizationsRepository.java
 * 文件描述: 组织机构 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsRepository extends BaseRepository<Organizations, Long> {
    /**
     * 批量修改状态
     * @param orgStatus  0：正常  1：禁用
     * @param updateTime
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update organizations org set org.org_status = ?1, org.update_time = ?2, org.data_version = data_version+1  where org.id in (?3)", nativeQuery = true)
    int setOrgStatusByIds(Byte orgStatus, Date updateTime, List<Long> ids);


    /**
     * 修改状态
     * @param orgStatus  0:启动 1：禁用
     * @param id
     * @param updateTime
     * @param version
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update organizations org set org.org_status = ?1, org.update_time = ?2, org.data_version = data_version+1  where org.id = ?3 and org.data_version = ?4", nativeQuery = true)
    int setStatusById(Byte orgStatus,Date updateTime, Long id, Long version);

}

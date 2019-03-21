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
     * 修改状态
     * @param orgStatus  0：正常  1：禁用
     * @param updateTime
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update Organizations u set u.orgStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setOrgStatusByIds(Byte orgStatus, Date updateTime, List<Long> ids);

    /**
     * 更新机构全名称
     * @param fullName
     * @param updateTime
     * @param id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update Organizations u set u.fullName = ?1,  u.updateTime = ?2 where u.id = ?3")
    int updateFullNameById(String fullName, Date updateTime, Long id);
}

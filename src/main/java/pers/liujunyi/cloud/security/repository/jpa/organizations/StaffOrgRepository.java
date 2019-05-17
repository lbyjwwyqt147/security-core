package pers.liujunyi.cloud.security.repository.jpa.organizations;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: StaffOrgRepository.java
 * 文件描述: 职工组织机构 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgRepository extends BaseRepository<StaffOrg, Long> {

    /**
     * 根据 id 修改状态
     * @param status  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update StaffOrg u set u.status = ?1, u.update_time = ?2, u.data_version = data_version+1  where u.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte status, Date updateTime, List<Long> ids);


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
    @Query(value = "update  StaffOrg u set u.status = ?1, u.update_time = ?2, u.data_version = data_version+1  where org.id = ?3 and u.data_version = ?4", nativeQuery = true)
    int setStatusById(Byte orgStatus,Date updateTime, Long id, Long version);

    /**
     * 根据 staffId 修改状态
     * @param status  0：正常  1：禁用
     * @param staffIds 职工id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update StaffOrg u set u.status = ?1, u.update_time = ?2 , u.data_version = data_version+1 where u.staffId in (?3)", nativeQuery = true)
    int setStatusByStaffIds(Byte status, Date updateTime, List<Long> staffIds);

    /**
     * 根据 staffId 修改状态
     * @param status  0：正常  1：禁用
     * @param staffId 职工id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update StaffOrg u set u.status = ?1, u.update_time = ?2 , u.data_version = data_version+1 where u.staffId = ?3", nativeQuery = true)
    int setStatusByStaffId(Byte status, Date updateTime, Long staffId);

    /**
     * 根据 staffId 删除
     * @param staffId 职工id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByStaffId(Long staffId);

    /**
     * 根据 staffId 删除
     * @param staffIds 职工id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByStaffIdIn(List<Long> staffIds);

    /**
     * 根据 orgId 删除
     * @param orgIds 组织机构id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByOrgIdIn(List<Long> orgIds);

    /**
     * 根据 orgId 删除
     * @param orgId 机构id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    long deleteByOrgId(Long orgId);
}

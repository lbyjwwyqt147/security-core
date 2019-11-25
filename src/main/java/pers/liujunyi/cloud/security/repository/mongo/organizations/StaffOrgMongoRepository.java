package pers.liujunyi.cloud.security.repository.mongo.organizations;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.List;

/***
 * 文件名称: StaffOrgMongoRepository.java
 * 文件描述: 职工关联组织机构 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgMongoRepository extends BaseMongoRepository<StaffOrg, Long> {


    /**
     * 根据 机构id 获取数据
     * @param orgId
     * @return
     */
    List<StaffOrg> findByOrgId(Long orgId);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @return
     */
    List<StaffOrg> findByOrgIdInOrderByIdAsc(List<Long> orgIds);

    /**
     * 根据 机构id 获取数据
     * @param orgIds
     * @return
     */
    List<StaffOrg> findByOrgIdIn(List<Long> orgIds);

    /**
     * 根据 职工id 获取数据
     * @param staffIds
     * @return
     */
    List<StaffOrg> findByStaffIdIn(List<Long> staffIds);


    /**
     * 根据 职工id 获取数据
     * @param staffId
     * @return
     */
    List<StaffOrg> findByStaffId(Long staffId);

    /**
     * 根据 职工id 获取数据
     * @param staffIds
     * @return
     */
    List<StaffOrg> findByStaffIdInOrderByIdAsc(List<Long> staffIds);


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

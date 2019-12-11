package pers.liujunyi.cloud.security.service.organizations;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseJpaMongoService;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.List;

/***
 * 文件名称: StaffOrgService.java
 * 文件描述:  职工关联组织机构 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgService extends BaseJpaMongoService<StaffOrg, Long> {

    /**
     * 保存数据
     * @param org  机构
     * @param staffIds 人员id
     * @return
     */
    ResultInfo saveRecord(StaffOrg org, List<Long> staffIds);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);

    /**
     * 根据职工id 修改状态
     * @param status   0：正常  1：禁用
     * @param staffIds
     * @return
     */
    int updateStatusByStaffIds(Byte status, List<Long> staffIds);

    /**
     * 根据 机构id 修改状态
     * @param status   0：正常  1：禁用
     * @param orgIds
     * @return
     */
    int updateStatusByOrgIds(Byte status, List<Long> orgIds);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids);

    /**
     * 单条删除
     * @param id
     * @return
     */
    ResultInfo deleteSingle(Long id);


    /**
     * 根据机构id单条删除
     * @param orgId
     * @return
     */
    long deleteByOrgId(Long orgId);

    /**
     * 根据机构id删除
     * @param orgIds
     * @return
     */
    long deleteByOrgIds(List<Long> orgIds);

    /**
     * 根据职工id单条删除
     * @param staffId
     * @return
     */
    long deleteByStaffId(Long staffId);

    /**
     * 根据职工id删除
     * @param staffIds
     * @return
     */
    long deleteByStaffIds(List<Long> staffIds);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToMongo();

}

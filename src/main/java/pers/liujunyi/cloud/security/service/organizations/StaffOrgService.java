package pers.liujunyi.cloud.security.service.organizations;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
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
public interface StaffOrgService extends BaseService<StaffOrg, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(StaffOrg record);

    /**
     * 修改状态
     * @param status   0：已发布（可见）  1：不可见  2：草稿
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo batchDeletes(List<Long> ids);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();

}

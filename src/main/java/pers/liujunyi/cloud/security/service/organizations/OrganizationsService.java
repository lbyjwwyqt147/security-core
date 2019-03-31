package pers.liujunyi.cloud.security.service.organizations;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: OrganizationsService.java
 * 文件描述:  组织机构 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface OrganizationsService extends BaseService<Organizations, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(OrganizationsDto record);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @param putParams
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids, String putParams);

    /**
     * 修改状态
     * @param status
     * @param id
     * @param version
     * @return
     */
    ResultInfo updateStatus(Byte status, Long id, Long version);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo batchDeletes(List<Long> ids);

    /**
     * 单条删除
     * @param id
     * @return
     */
    ResultInfo singleDelete(Long id);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();


}

package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;

import java.util.List;

/***
 * 文件名称: PositionInfoService.java
 * 文件描述:  岗位信息 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface PositionInfoService extends BaseService<PositionInfo, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(PositionInfoDto record);

    /**
     * 修改状态
     * @param status   0：正常  1：禁用
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids);


    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids);


    /**
     * 同步数据到Mongo中
     * @return
     */
    ResultInfo syncDataToMongo();
}

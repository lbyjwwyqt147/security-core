package pers.liujunyi.cloud.security.service.category;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseJpaMongoService;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoDto;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;

import java.util.List;

/***
 * 文件名称: CategoryInfoService.java
 * 文件描述:  分类信息 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CategoryInfoService extends BaseJpaMongoService<CategoryInfo, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(CategoryInfoDto record);

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
    ResultInfo syncDataToElasticsearch();

}

package pers.liujunyi.cloud.security.service.category;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoQueryDto;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: CategoryInfoElasticsearchService.java
 * 文件描述: 分类信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CategoryInfoElasticsearchService extends BaseElasticsearchService<CategoryInfo, Long> {



    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(CategoryInfoQueryDto query);


    /**
     * 根据 帐号id 获取员工详细数据
     * @param id
     * @return
     */
    CategoryInfo findById(Long id);


    /**
     * 分类下拉框数据
     * @param query
     * @return
     */
    List<Map<String, String>> categorySelect(CategoryInfoQueryDto query);

    /**
     * 获取分类名称
     * @param ids
     * @return
     */
    Map<Long, String> getCategoryNameMap(List<Long> ids);

    /**
     * 验证名称是否重复
     * @param categoryType  类型  10：流程分类
     * @param categoryName  最新值
     * @param history  历史值
     * @return
     */
    String verifyCategoryName(Byte categoryType, String categoryName, String history);

}

package pers.liujunyi.cloud.security.repository.mongo.category;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;

import java.util.List;

/***
 * 文件名称: CategoryInfoMongoRepository.java
 * 文件描述: 分类信息 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface CategoryInfoMongoRepository extends BaseMongoRepository<CategoryInfo, Long> {

    /**
     * 根据类型 和状态 获取数据
     * @param categoryType  类型  10:流程分类
     * @param categoryStatus  0:正常  1：禁用
     * @return
     */
    List<CategoryInfo> findByCategoryTypeAndCategoryStatus(Byte categoryType, Byte categoryStatus);

    /**
     * 根据categoryType 和 categoryName 获取数据
     * @param categoryType  类型  10:流程分类
     * @param categoryName  名称
     * @return
     */
    List<CategoryInfo> findByCategoryTypeAndCategoryName(Byte categoryType, String categoryName);
}

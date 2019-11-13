package pers.liujunyi.cloud.security.repository.jpa.category;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: CategoryInfoRepository.java
 * 文件描述: 分类信息 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface CategoryInfoRepository extends BaseRepository<CategoryInfo, Long> {

    /**
     * 修改状态
     * @param categoryStatus  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update category_info u set u.category_status = ?1, u.update_time = ?2 where u.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte categoryStatus, Date updateTime, List<Long> ids);

}

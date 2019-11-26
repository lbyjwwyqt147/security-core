package pers.liujunyi.cloud.security.repository.jpa.authorization;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: MenuResourceRepository.java
 * 文件描述: 菜单资源 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface MenuResourceRepository extends BaseRepository<MenuResource, Long> {

    /**
     * 修改状态
     * @param menuStatus  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update menu_resource u set u.menu_status = ?1, u.update_time = ?2 where u.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte menuStatus, Date updateTime, List<Long> ids);

}

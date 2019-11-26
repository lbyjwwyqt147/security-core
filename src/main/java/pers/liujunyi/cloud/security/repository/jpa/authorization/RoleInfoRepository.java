package pers.liujunyi.cloud.security.repository.jpa.authorization;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: RoleInfoRepository.java
 * 文件描述: 角色信息 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Repository
public interface RoleInfoRepository extends BaseRepository<RoleInfo, Long> {
    /**
     * 修改状态
     * @param roleStatus  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query(value = "update role_info u set u.role_status = ?1, u.update_time = ?2 where u.id in (?3)", nativeQuery = true)
    int setStatusByIds(Byte roleStatus, Date updateTime, List<Long> ids);

}

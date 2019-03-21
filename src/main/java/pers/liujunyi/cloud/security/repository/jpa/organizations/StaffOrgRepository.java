package pers.liujunyi.cloud.security.repository.jpa.organizations;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: StaffOrgRepository.java
 * 文件描述: 职工组织机构 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface StaffOrgRepository extends BaseRepository<StaffOrg, Long> {

    /**
     * 修改状态
     * @param status  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update StaffOrg u set u.status = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setStatusByIds(Byte status, Date updateTime, List<Long> ids);


}

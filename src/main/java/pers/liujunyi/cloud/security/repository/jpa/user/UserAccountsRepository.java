package pers.liujunyi.cloud.security.repository.jpa.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: UserAccountsRepository.java
 * 文件描述: 用户账户 Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsRepository extends BaseRepository<UserAccounts, Long> {

    /**
     * 修改状态
     * @param userStatus  0：正常  1：禁用
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update UserAccounts u set u.userStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setUserStatusByIds(Byte userStatus, Date updateTime, List<Long> ids);

    /**
     * 修改用户类型
     * @param userCategory  2：员工  3：顾客
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update UserAccounts u set u.userCategory = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setUserCategoryByIds(Byte userCategory, Date updateTime, List<Long> ids);

    /**
     * 修改用户密码
     * @param userPassword
     * @param id
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Modifying(clearAutomatically = true)
    @Query("update UserAccounts u set u.userPassword = ?1, u.changePasswordTime = ?2, u.updateTime = ?2 where u.id = ?3")
    int setUserPasswordById(String userPassword, Date time, Long id);

}

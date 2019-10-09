package pers.liujunyi.cloud.security.repository.elasticsearch.user;

import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;

import java.util.List;

/***
 * 文件名称: UserAccountsElasticsearchRepository.java
 * 文件描述: 用户帐号信息 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsElasticsearchRepository extends BaseElasticsearchRepository<UserAccounts, Long> {

    /**
     * 根据手机号获取信息
     * @param mobilePhone
     * @return
     */
    UserAccounts findFirstByMobilePhone(String mobilePhone);

    /**
     * 根据 userAccounts 获取信息
     * @param userAccounts
     * @return
     */
    UserAccounts findFirstByUserAccounts(String userAccounts);

    /**
     * 根据 userNumber 获取信息
     * @param userNumber
     * @return
     */
    UserAccounts findFirstByUserNumber(String userNumber);

    /**
     * 用户帐号登录
     * @param userAccounts  帐号
     * @param userPassword 密码
     * @return
     */
    UserAccounts findFirstByUserAccountsAndUserPassword(String userAccounts, String userPassword);

    /**
     * 用户手机登录
     * @param mobilePhone 手机号
     * @param userPassword 密码
     * @return
     */
    UserAccounts findFirstByMobilePhoneAndUserPassword(String mobilePhone, String userPassword);

    /**
     * 用户编号 登录
     * @param userNumber 用户编号
     * @param userPassword 密码
     * @return
     */
    UserAccounts findFirstByUserNumberAndUserPassword(String userNumber, String userPassword);

    /**
     * 根据 userAccounts  mobilePhone  userNumber   任意一个登录
     * @param userAccounts
     * @param userPassword
     * @return
     */
    UserAccounts findFirstByUserAccountsOrMobilePhoneOrUserNumberAndUserPassword(String userAccounts, String mobilePhone, String userNumber, String userPassword);

    /**
     * 根据 userAccounts  mobilePhone  userNumber   任意一个获取信息
     * @param userAccounts
     * @return
     */
    UserAccounts findFirstByUserAccountsOrMobilePhoneOrUserNumber(String userAccounts, String mobilePhone, String userNumber);

    /**
     * 排除 指定 id
     * @param ids
     * @return
     */
    List<UserAccounts>  findByIdNotIn(List<Long> ids);

    /**
     * 根据用户状态 获取信息
     * @param userStatus
     * @return
     */
    List<UserAccounts> findByUserStatus(Byte userStatus);
}

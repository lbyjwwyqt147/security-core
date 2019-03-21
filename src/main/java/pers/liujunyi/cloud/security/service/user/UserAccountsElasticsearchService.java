package pers.liujunyi.cloud.security.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseElasticsearchService;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;

/***
 * 文件名称: UserAccountsElasticsearchService.java
 * 文件描述: 用户账户信息 Elasticsearch Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsElasticsearchService extends BaseElasticsearchService<UserAccounts, Long> {

    /**
     * 用户登录
     * @param userAccounts
     * @param userPassword
     * @return
     */
    ResultInfo userLogin(String userAccounts, String userPassword);

    /**
     * 根据 userAccounts  mobilePhone  userNumber   任意一个获取信息
     * @param userAccounts
     * @return
     */
    UserAccounts findFirstByUserAccountsOrMobilePhoneOrUserNumber(String userAccounts);
}

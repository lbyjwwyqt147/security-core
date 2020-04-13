package pers.liujunyi.cloud.security.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.security.domain.user.UserAccountsQueryDto;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: UserAccountsMongoService.java
 * 文件描述: 用户账户信息 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsMongoService extends BaseMongoService<UserAccounts, Long> {

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

    /**
     * 根据id 获取用户信息 并转为 map
     * @param ids
     * @return  key = id  value = UserAccounts
     */
    Map<Long, UserAccounts> getUserAccountInfoToMap(List<Long> ids);

    /**
     * 根据id 获取用户名称 并转为 map
     * @param ids
     * @return  key = id  value = name
     */
    Map<Long, String> getUserNameToMap(List<Long> ids);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(UserAccountsQueryDto query);

    /**
     * 根据token 获取用户详情
     * @param token
     * @return
     */
    ResultInfo getUserByToken(String token);

    /**
     * 根据id获取用户详情
     * @param id
     * @return
     */
    ResultInfo getUserById(Long id);

}

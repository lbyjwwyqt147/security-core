package pers.liujunyi.cloud.security.service.user;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseService;
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsUpdateDto;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;

import java.util.List;

/***
 * 文件名称: UserAccountsService.java
 * 文件描述:  用户帐号信息 Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsService extends BaseService<UserAccounts, Long> {

    /**
     * 保存数据
     * @param record
     * @return
     */
    ResultInfo saveRecord(UserAccountsDto record);

    /**
     * 保存数据
     * @param record
     * @return  返回 主键id
     */
    String saveUserAccountsRecord(UserAccountsDto record);

    /**
     * 修改状态
     * @param status    0：正常  1：禁用
     * @param putParams
     * @param ids
     * @return
     */
    ResultInfo updateStatus(Byte status, List<Long> ids, String putParams);

    /**
     * 修改状态
     * @param status
     * @param id
     * @param dataVersion
     * @return
     */
    ResultInfo updateStatus(Byte status, Long id, Long dataVersion);

    /**
     * 修改状态
     * @param status
     * @param id
     * @return
     */
    Boolean updateAccountsStatus(Byte status, Long id, Long dataVersion);

    /**
     * 修改用户状态
     * @param status    0：正常  1：禁用
     * @param ids
     * @return
     */
    Boolean updateUserAccountsStatus(Byte status, List<Long> ids, String putParams);

    /**
     * 修改密码
     * @param id
     * @param historyPassWord  历史密码  如果历史密码 ==
     * @param currentPassWord  新密码
     * @return
     */
    ResultInfo updateUserPassWord(Long id, String historyPassWord, String currentPassWord, Long dataVersion);

    /**
     * 更新  userAccounts   userPassword  mobilePhone  userMailbox  值
     * @param userAccountsUpdate
     * @return
     */
    ResultInfo updateUserAccountsInfo(UserAccountsUpdateDto userAccountsUpdate);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    ResultInfo deleteBatch(List<Long> ids);

    /**
     * 单条删除
     * @param id
     * @return
     */
    ResultInfo deleteSingle(Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    Boolean deleteByUserAccounts(List<Long> ids);

    /**
     * 同步数据到es中
     * @return
     */
    ResultInfo syncDataToElasticsearch();

    /**
     * 同步数据到es中
     * @return
     */
    void userAccountsSyncDataToElasticsearch();

}

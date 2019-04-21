package pers.liujunyi.cloud.security.domain.user;

import lombok.Data;

import java.io.Serializable;

/***
 * 文件名称: UserAccountsUpdateDto.java
 * 文件描述: 更新账户信息
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
public class UserAccountsUpdateDto implements Serializable {
    private static final long serialVersionUID = -1132076240884946955L;

    private Long id;

    /** 用户帐号 */
    private String userAccounts;

    /** 用户编号  */
    private String userNumber;

    /** 用户名称 */
    private String userName;

    /** 用户昵称 */
    private String userNickName;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 电子邮箱 */
    private String userMailbox;

    /** 版本号  */
    private Long dataVersion;

}

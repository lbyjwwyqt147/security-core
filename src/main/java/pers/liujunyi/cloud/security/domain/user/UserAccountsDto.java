package pers.liujunyi.cloud.security.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.*;
import java.util.Date;

/***
 * 文件名称: UserAccountsDto.java
 * 文件描述: 账户信息
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountsDto extends BaseDto {

    private static final long serialVersionUID = -7365280455089087793L;

    private Long id;

    /** 用户帐号 */
    @ApiModelProperty(value = "用户帐号")
    @NotBlank(message = "帐号 必须填写")
    @Length(min = 5, max = 65, message = "帐号 最多可以输入65个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_ACCOUNT_REGEXP, message = "帐号 " + RegexpUtils.ALNUM_ACCOUNT_MSG)
    private String userAccounts;

    /** 用户编号  */
    @ApiModelProperty(value = "用户编号")
    @NotBlank(message = "用户编号 必须填写")
    @Length(min = 1, max = 20, message = "用户编号 最多可以输入20个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "用户编号 " + RegexpUtils.ALNUM_CODE_MSG)
    private String userNumber;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    @NotBlank(message = "用户名称 必须填写")
    @Length(min = 1, max = 32, message = "用户名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "用户名称 " + RegexpUtils.ALNUM_NAME_MSG)
    private String userName;

    /** 用户昵称 */
    @ApiModelProperty(value = "用户昵称")
    @Length(min = 0, max = 32, message = "昵称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "昵称 " + RegexpUtils.ALNUM_NAME_MSG)
    private String userNickName;

    /** 用户密码 */
    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码 必须填写")
    @Length(min = 6, max = 32, message = "帐号 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_PWD_REGEXP, message = "密码 " + RegexpUtils.ALNUM_PWD_MSG)
    private String userPassword;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    @Length(min = 0, max = 11, message = "手机号 位数为11位")
    @Pattern(regexp = RegexpUtils.MOBILE_PHONE_REGEXP, message = RegexpUtils.MOBILE_PHONE_MSG)
    private String mobilePhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    @Length(min = 0, max = 65, message = "电子邮箱 最多可以输入65个字符")
    @Email(message = "电子邮箱格式错误")
    private String userMailbox;

    /** 状态：0：正常  1：冻结 */
    @ApiModelProperty(value = "状态")
    @Min(value = 0, message = "状态 值必须大于0")
    @Max(value = 127, message = "状态 最大值不能大于127")
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    @ApiModelProperty(value = "用户类别")
    @NotNull(message = "用户类别 必须填写")
    @Min(value = 0, message = "用户类别 值必须大于0")
    @Max(value = 127, message = "用户类别 最大值不能大于127")
    private Byte userCategory;

    /** 注册时间  */
    private Date registrationTime;

}

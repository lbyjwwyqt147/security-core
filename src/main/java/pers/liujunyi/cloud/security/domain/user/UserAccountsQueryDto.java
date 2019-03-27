package pers.liujunyi.cloud.security.domain.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;

/***
 * 文件名称: UserAccountsQueryDto.java
 * 文件描述: 账户信息 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountsQueryDto  extends BaseEsQuery {

    private static final long serialVersionUID = 4758601347679390962L;
    /** 用户帐号 */
    @ApiModelProperty(value = "用户帐号")
    @QueryCondition
    private String userAccounts;

    /** 用户编号  */
    @ApiModelProperty(value = "用户编号")
    @QueryCondition
    private String userNumber;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    @QueryCondition(func = MatchType.or)
    private String userName;

    /** 用户昵称 */
    @ApiModelProperty(value = "用户昵称")
    @QueryCondition(func = MatchType.or)
    private String userNickName;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    @QueryCondition
    private String mobilePhone;

    /** 电子邮箱 */
    @ApiModelProperty(value = "电子邮箱")
    @QueryCondition
    private String userMailbox;

    /** 状态：0：正常  1：冻结 */
    @ApiModelProperty(value = "状态")
    @QueryCondition
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    @ApiModelProperty(value = "用户类别")
    @QueryCondition
    private Byte userCategory;

}

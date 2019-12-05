package pers.liujunyi.cloud.security.domain.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/***
 * 文件名称: RoleInfoDto.java
 * 文件描述: 角色信息 dto
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleInfoDto  extends BaseDto {

    private static final long serialVersionUID = 4568418491964707673L;
    /** 角色编号 */
    @ApiModelProperty(value = "角色编号")
    @NotBlank(message = "角色编号 必须填写")
    @Length(min = 1, max = 15, message = "角色编号 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.CODE_REGEXP, message = "编号 " + RegexpUtils.CODE_MSG)
    private String roleNumber;

    /** 角色名称 */
    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称 必须填写")
    @Length(min = 1, max = 32, message = "角色名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.HANZI_REGEXP, message = "名称 " + RegexpUtils.HANZI_MSG)
    private String roleName;

    /** 父级主键id */
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态：0：正常  1：禁用")
    private Byte roleStatus;

    /** 角色授权代码 授权代码需要加ROLE_前缀 */
    @ApiModelProperty(value = "角色授权代码")
    @NotBlank(message = "角色授权代码 必须填写")
    @Length(min = 1, max = 15, message = "角色授权代码 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.ENGLISH_LETTER_REGEXP, message = "角色授权代码 " + RegexpUtils.ENGLISH_LETTER_MSG)
    private String roleAuthorizationCode;

    /** 备注描述 */
    @ApiModelProperty(value = "备注描述")
    @Length(min = 0, max = 50, message = "备注描述 最多可以输入100个字符")
    private String roleDescription;
}

package pers.liujunyi.cloud.security.domain.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.persistence.Column;
import javax.validation.constraints.*;

/***
 * 文件名称: MenuResourceDto.java
 * 文件描述: 菜单资源 dto
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
public class MenuResourceDto extends BaseDto {

    private static final long serialVersionUID = -5617973632379890070L;
    /** 菜单资源编号 */
    @ApiModelProperty(value = "编号")
    @NotBlank(message = "编号 必须填写")
    @Length(min = 1, max = 15, message = "编号 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.CODE_REGEXP, message = "编号 " + RegexpUtils.CODE_MSG)
    private String menuNumber;

    /** 菜单资源名称 */
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称 必须填写")
    @Length(min = 1, max = 32, message = "名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.HANZI_REGEXP, message = "名称 " + RegexpUtils.HANZI_MSG)
    private String menuName;

    /** 资源类型 1:目录  2：菜单界面   3：功能按钮 */
    @ApiModelProperty(value = "资源类型 1:目录  2：菜单界面   3：功能按钮")
    @NotNull(message = "资源类型 必须填写")
    @Min(value = 0, message = "资源类型 值必须大于0")
    @Max(value = 127, message = "资源类型 最大值不能大于127")
    private Byte menuClassify;

    /** 菜单资源图标 */
    @ApiModelProperty(value = "菜单资源图标")
    @Length(min = 1, max = 32, message = "菜单资源图标 最多可以输入32个字符")
    private String menuIcon;

    /** 菜单资源路径 */
    @ApiModelProperty(value = "菜单资源路径")
    @Length(min = 1, max = 255, message = "菜单资源路径 最多可以输入32个字符")
    @Column(columnDefinition="varchar(255) DEFAULT NULL COMMENT '菜单资源路径'")
    private String menuPath;

    /** 排序值 */
    @ApiModelProperty(value = "排序值")
    @Min(value = 0, message = "资源类型 值必须大于0")
    @Max(value = 127, message = "资源类型 最大值不能大于127")
    private Byte serialNumber;

    /** 层级 */
    @ApiModelProperty(value = "层级")
    private Byte menuLevel;

    /** 父级主键id */
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态：0：正常  1：禁用")
    private Byte menuStatus;

    /** 权限授权代码  授权代码不需要加ROLE_前缀 */
    @ApiModelProperty(value = "权限授权代码")
    @Length(min = 1, max = 15, message = "权限授权代码 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.ENGLISH_LETTER_REGEXP, message = "权限授权代码 " + RegexpUtils.ENGLISH_LETTER_MSG)
    private String menuAuthorizationCode;

    /** 完整的层次名称 */
    private String fullMenuName;

    /** 完整的层级Id */
    private String fullMenuParent;

    /** 完整的层级代码 */
    private String fullMenuParentCode;

    /** 备注描述 */
    @ApiModelProperty(value = "备注描述")
    @Length(min = 1, max = 100, message = "备注描述 最多可以输入100个字符")
    private String menuDescription;

    /** 预留字段1 */
    @ApiModelProperty(value = "预留字段1")
    @Length(min = 1, max = 45, message = "预留字段1 最多可以输入45个字符")
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "预留字段2")
    @Length(min = 1, max = 65, message = "预留字段2 最多可以输入65个字符")
    private String attributeTwo;

}

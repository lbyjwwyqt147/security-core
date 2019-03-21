package pers.liujunyi.cloud.security.domain.organizations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.*;

/***
 * 文件名称: OrganizationsDto.java
 * 文件描述: 组织机构
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月11日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationsDto extends BaseDto {
    private static final long serialVersionUID = -2904937024962048531L;
    /** 机构编号 */
    @ApiModelProperty(value = "机构代码")
    @NotBlank(message = "机构代码必须填写")
    @Length(min = 1, max = 15, message = "机构代码 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "机构代码 " + RegexpUtils.ALNUM_CODE_MSG)
    private String orgNumber;

    /** 机构名称 */
    @ApiModelProperty(value = "机构名称")
    @NotBlank(message = "机构名称必须填写")
    @Length(min = 1, max = 32, message = "机构名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "机构名称 " + RegexpUtils.ALNUM_NAME_MSG)
    private String orgName;

    /** 机构级别 */
    @ApiModelProperty(value = "机构级别")
    @Max(value = 127, message = "机构级别 最大值不能大于127")
    private Byte orgLevel;

    /** 父级主键id */
    @ApiModelProperty(value = "上级机构ID")
    @NotNull(message = "上级机构必须填写")
    private Long parentId;

    /** 排序号 */
    @ApiModelProperty(value = "序号")
    @Max(value = 1000, message = "优先级 最大值不能大于1000")
    private Integer seq;

    /** 完整的机构名称 */
    private String fullName;

    /** 完整的层级 */
    private String fullParent;

    /** 完整的层级代码 */
    private String fullParentCode;

    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    @Length(min = 0, max = 100, message = "描述 最多可以输入100个字符")
    @Pattern(regexp = RegexpUtils.ILLEGITMACY_REGEXP, message = "描述 " + RegexpUtils.ILLEGITMACY_MSG)
    private String description;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态")
    @NotNull(message = "状态 必须填写")
    @Min(value = 0, message = "状态 必须是数字类型")
    @Max(value = 127, message = "状态 最大值不能大于127")
    private Byte orgStatus;

    /** 预留字段1 */
    @ApiModelProperty(value = "attributeOne")
    @Length(min = 0, max = 45, message = "attributeOne 最多可以输入45个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeOne " + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "attributeTwo")
    @Length(min = 0, max = 65, message = "attributeTwo 最多可以输入65个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeTwo " + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeTwo;

    /** 预留字段3 */
    @ApiModelProperty(value = "attributeThree")
    @Length(min = 0, max = 100, message = "attributeThree 最多可以输入100个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_NAME_REGEXP, message = "attributeThree " + RegexpUtils.ALNUM_NAME_MSG)
    private String attributeThree;
}
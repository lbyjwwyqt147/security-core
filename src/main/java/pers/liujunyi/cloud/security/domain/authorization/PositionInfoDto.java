package pers.liujunyi.cloud.security.domain.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import pers.liujunyi.cloud.common.dto.BaseDto;
import pers.liujunyi.cloud.common.util.RegexpUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/***
 * 文件名称: PositionInfoDto.java
 * 文件描述: 岗位信息 dto
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
public class PositionInfoDto extends BaseDto {

    private static final long serialVersionUID = 1720289376082797845L;

    /** 岗位编号 */
    @ApiModelProperty(value = "编号")
    @NotBlank(message = "编号 必须填写")
    @Length(min = 1, max = 15, message = "编号 最多可以输入15个字符")
    @Pattern(regexp = RegexpUtils.ALNUM_CODE_REGEXP, message = "编号 " + RegexpUtils.ALNUM_CODE_MSG)
    private String postNumber;

    /** 岗位名称 */
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称 必须填写")
    @Length(min = 1, max = 32, message = "名称 最多可以输入32个字符")
    @Pattern(regexp = RegexpUtils.HANZI_REGEXP, message = "名称 " + RegexpUtils.HANZI_MSG)
    private String postName;

    /** 层级 */
    private Byte postLevel;

    /** 排序值 */
    @ApiModelProperty(value = "排序值")
    @Min(value = 0, message = "资源类型 值必须大于0")
    @Max(value = 127, message = "资源类型 最大值不能大于127")
    private Byte serialNumber;

    /** 父级主键id */
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态：0：正常  1：禁用")
    private Byte postStatus;


    /** 完整的层次名称 */
    private String fullPostName;

    /** 完整的层级Id */
    private String fullPostParent;

    /** 完整的层级代码 */
    private String fullPostParentCode;

    /** 备注描述 */
    @ApiModelProperty(value = "备注描述")
    @Length(min = 1, max = 100, message = "备注描述 最多可以输入100个字符")
    private String postDescription;

    /** 预留字段1 */
    @ApiModelProperty(value = "预留字段1")
    @Length(min = 1, max = 45, message = "预留字段1 最多可以输入45个字符")
    private String attributeOne;

    /** 预留字段2 */
    @ApiModelProperty(value = "预留字段2")
    @Length(min = 1, max = 65, message = "预留字段2 最多可以输入65个字符")
    private String attributeTwo;
}

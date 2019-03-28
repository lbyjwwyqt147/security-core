package pers.liujunyi.cloud.security.domain.organizations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Version;
import pers.liujunyi.cloud.common.vo.BaseVo;

/***
 * 文件名称: OrganizationsVo.java
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
public class OrganizationsVo extends BaseVo {
    private static final long serialVersionUID = -8838645540403216851L;
    /** 机构编号 */
    @ApiModelProperty(value = "机构编号")
    private String orgNumber;

    /** 机构名称 */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    /** 机构级别 */
    @ApiModelProperty(value = "机构级别")
    private Byte orgLevel;

    /** 父级主键id */
    @ApiModelProperty(value = "上级机构ID")
    private Long parentId;

    /** 排序号 */
    @ApiModelProperty(value = "序号")
    private Integer seq;

    /** 完整的机构名称 */
    @ApiModelProperty(value = "完整的机构名称")
    private String fullName;

    /** 描述说明 */
    @ApiModelProperty(value = "描述说明")
    private String description;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "状态")
    private Byte orgStatus;

    /** 完整的层级代码 */
    private String fullParentCode;

    /** 上级机构名称 */
    private String organizationParentName;

    /** 版本号 */
    private Long version;

}
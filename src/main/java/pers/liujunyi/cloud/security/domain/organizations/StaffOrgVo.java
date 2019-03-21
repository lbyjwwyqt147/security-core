package pers.liujunyi.cloud.security.domain.organizations;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * 文件名称: StaffOrgVo.java
 * 文件描述: 组织机构关联人员  vo
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
public class StaffOrgVo implements Serializable {

    private static final long serialVersionUID = -7518917375885077681L;

    private Long id;

    /** 机构ID */
    @ApiModelProperty(value = "机构id")
    private Long orgId;

    /** 职工id */
    @ApiModelProperty(value = "职工id")
    private Long staffId;

    /** 状态：0：正常  1：禁用 */
    @ApiModelProperty(value = "职工id")
    private Byte status;

    /** 用户编号  */
    @ApiModelProperty(value = "用户编号")
    private String userNumber;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

}

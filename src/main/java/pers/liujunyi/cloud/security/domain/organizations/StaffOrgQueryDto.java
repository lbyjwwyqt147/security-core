package pers.liujunyi.cloud.security.domain.organizations;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;
import pers.liujunyi.cloud.common.query.mongo.BaseMongoQuery;

/***
 * 文件名称: StaffOrgQueryDto.java
 * 文件描述: 组织机构关联人员 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class StaffOrgQueryDto extends BaseMongoQuery {

    private static final long serialVersionUID = 570038382505298728L;
    /** 机构id */
    @ApiModelProperty(value = "机构id")
    @QueryCondition()
    private Long orgId;

    /** 完整的层级Id */
    @ApiModelProperty(value = "完整的层级Id")
    @QueryCondition(func = MatchType.like)
    private String fullParent;

    /** 机构编号 */
    @ApiModelProperty(value = "机构编号")
    @QueryCondition()
    private String orgNumber;

    /** 用户名称 */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /** 绑定的手机号 */
    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

}

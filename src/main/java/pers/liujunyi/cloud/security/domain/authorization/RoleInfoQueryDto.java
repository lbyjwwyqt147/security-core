package pers.liujunyi.cloud.security.domain.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;
import pers.liujunyi.cloud.common.query.mongo.BaseMongoQuery;

/***
 * 文件名称: RoleInfoQueryDto.java
 * 文件描述: 角色信息 query dto
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleInfoQueryDto extends BaseMongoQuery {

    private static final long serialVersionUID = 5504924481837457087L;

    @ApiModelProperty(value = "pid")
    @QueryCondition()
    private Long parentId;

    @ApiModelProperty(value = "名称")
    @QueryCondition(func = MatchType.like)
    private String roleName;
}

package pers.liujunyi.cloud.security.domain.category;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.query.elasticsearch.BaseEsQuery;
import pers.liujunyi.cloud.common.query.jpa.annotation.MatchType;
import pers.liujunyi.cloud.common.query.jpa.annotation.QueryCondition;


/***
 * 文件名称: CategoryInfoQueryDto.java
 * 文件描述: 分类信息 query dto
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
public class CategoryInfoQueryDto extends BaseEsQuery {
    private static final long serialVersionUID = 7567804107830388170L;

    /** 名称 */
    @ApiModelProperty(value = "名称")
    @QueryCondition(func = MatchType.like)
    private String categoryName;

    /** 类型 10: 流程分类 */
    @ApiModelProperty(value = "类型： 10: 流程分类 ")
    @QueryCondition()
    private Byte categoryType;

    /**  状态：0：正常  1：禁用  */
    @ApiModelProperty(value = "状态：0：正常  1：禁用 ")
    @QueryCondition()
    private Byte categoryStatus;
}

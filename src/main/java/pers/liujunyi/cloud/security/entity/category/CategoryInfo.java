package pers.liujunyi.cloud.security.entity.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: CategoryInfo.java
 * 文件描述: 分类信息
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_category_info", type = "categoryInfo", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class CategoryInfo extends BaseEntity {

    private static final long serialVersionUID = 3465433174018298922L;
    /** 编号 */
    private String categoryCode;

    /** 分类名称 */
    private String categoryName;

    /** 0：正常  1：禁用 */
    private Byte categoryStatus;

    /** 排序值 */
    private Integer sequenceNumber;

    /** 租户 */
    private Long lessee;

    /** 10: 流程分类 */
    private Byte categoryType;

    /** 备注表述 */
    private String description;

    /** 完整的名称 */
    private String fullName;

    /** 完整的父ID */
    private String fullParent;

    /** 父ID */
    private Long parentId;

    /** 完整的父级编号 */
    private String fullParentCode;

    /** 层次 */
    private Byte level;
}
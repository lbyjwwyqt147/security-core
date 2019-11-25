package pers.liujunyi.cloud.security.entity.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Column;
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
@Document(collection = "categoryInfo")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "category_info", comment = "分类信息表")
public class CategoryInfo extends BaseEntity {

    private static final long serialVersionUID = 3465433174018298922L;
    /** 编号 */
    @Column(length = 20, columnDefinition="COMMENT '编号'")
    private String categoryCode;

    /** 分类名称 */
    @Column(length = 32, columnDefinition="COMMENT '分类名称'")
    private String categoryName;

    /** 0：正常  1：禁用 */
    @Column(columnDefinition="DEFAULT '0' COMMENT '状态： 0：正常  1：禁用'")
    @Indexed
    private Byte categoryStatus;

    /** 排序值 */
    @Column(columnDefinition="DEFAULT '10' COMMENT '排序值'")
    private Integer sequenceNumber;

    /** 租户 */
    @Column(columnDefinition="COMMENT '租户'")
    private Long lessee;

    /** 10: 流程分类 */
    @Column(nullable = false, columnDefinition="COMMENT '类型： 10: 流程分类 '")
    @Indexed
    private Byte categoryType;

    /** 备注描述 */
    @Column(length = 100, columnDefinition="COMMENT '备注描述'")
    private String description;

    /** 完整的名称 */
    @Column(length = 128, columnDefinition="COMMENT '完整的名称'")
    private String fullName;

    /** 完整的父ID */
    @Column(length = 45, columnDefinition="DEFAULT '0' COMMENT '完整的父ID'")
    private String fullParent;

    /** 父ID */
    @Column(columnDefinition="DEFAULT '0' COMMENT '父ID'")
    private Long parentId;

    /** 完整的父级编号 */
    @Column(columnDefinition="COMMENT '完整的父级编号'")
    private String fullParentCode;

    /** 层次 */
    @Column(columnDefinition="DEFAULT '1' COMMENT '完整的父级编号'")
    private Byte level;

}
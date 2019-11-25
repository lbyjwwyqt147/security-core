package pers.liujunyi.cloud.security.entity.organizations;

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
import javax.persistence.Version;

/***
 * 文件名称: Organizations.java
 * 文件描述: 组织机构
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "organizations")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "organizations", comment = "组织机构表")
public class Organizations extends BaseEntity {
    private static final long serialVersionUID = -2904937024962048531L;

    /** 机构编号 */
    @Column(length = 15, nullable = false, columnDefinition="COMMENT '机构编号'")
    private String orgNumber;

    /** 机构名称 */
    @Column(length = 32, nullable = false, columnDefinition="COMMENT '机构名称'")
    private String orgName;

    /** 机构级别 */
    @Column(columnDefinition="DEFAULT '1' COMMENT '机构级别'")
    private Byte orgLevel;

    /** 父级主键id */
    @Column(columnDefinition="DEFAULT '0' COMMENT '父级主键id'")
    @Indexed
    private Long parentId;

    /** 排序值 */
    @Column(columnDefinition="DEFAULT '10' COMMENT '排序值'")
    private Integer seq;

    /** 完整的机构名称 */
    @Column(length = 128, columnDefinition="COMMENT '完整的机构名称'")
    private String fullName;

    /** 完整的层级Id */
    @Column(length = 45, columnDefinition="COMMENT '完整的层级Id'")
    private String fullParent;

    /** 完整的层级代码 */
    @Column(length = 100, columnDefinition="COMMENT '完整的层级代码'")
    private String fullParentCode;

    /** 描述说明 */
    @Column(length = 100, columnDefinition="COMMENT '描述说明'")
    private String description;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte orgStatus;

    /** 预留字段1 */
    @Column(length = 45, columnDefinition="COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @Column(length = 65, columnDefinition="COMMENT '预留字段2'")
    private String attributeTwo;

    /** 预留字段3 */
    @Column(length = 100, columnDefinition="COMMENT '预留字段3'")
    private String attributeThree;

    @Version
    @Override
    public Long getDataVersion() {
        return super.getDataVersion();
    }
}
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

/***
 * 文件名称: StaffOrg.java
 * 文件描述: 职工关联组织机构
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
@Document(collection = "staff_org")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "staff_org", comment = "职工关联组织机构表")
public class StaffOrg extends BaseEntity {

    private static final long serialVersionUID = 7025563314086782146L;
    /** 机构ID */
    @Column(nullable = false, columnDefinition="bigint(20) NOT NULL COMMENT '机构ID'")
    @Indexed
    private Long orgId;

    /** 职工id */
    @Column(nullable = false, columnDefinition="bigint(20)  NOT NULL COMMENT '职工id'")
    @Indexed
    private Long staffId;

    /** 完整的层级Id */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '完整的层级Id'")
    private String fullParent;

    /** 机构编号 */
    @Column(length = 15, columnDefinition="varchar(15) DEFAULT NULL COMMENT '机构编号'")
    private String orgNumber;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte status;

}
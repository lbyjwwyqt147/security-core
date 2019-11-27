package pers.liujunyi.cloud.security.entity.authorization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/***
 * 文件名称: PositionInfo.java
 * 文件描述: 岗位信息
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
@Document(collection = "positionInfo")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "position_info", comment = "岗位信息表")
public class PositionInfo extends BaseEntity {

    /** 岗位编号 */
    @Column(length = 15, nullable = false, columnDefinition="varchar(15) NOT NULL COMMENT '岗位编号'")
    private String postNumber;

    /** 岗位名称 */
    @Column(length = 32,  columnDefinition="varchar(32) NOT NULL COMMENT '岗位名称'")
    private String postName;

    /** 层级 */
    @Column(columnDefinition="tinyint(4) DEFAULT '1' COMMENT '层级'")
    private Byte postLevel;

    /** 排序值 */
    @Column(columnDefinition="tinyint(4) DEFAULT '10' COMMENT '排序值'")
    private Byte serialNumber;

    /** 父级主键id */
    @Column(columnDefinition="bigint(20) DEFAULT '0' COMMENT '父级主键id'")
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte postStatus;


    /** 完整的层次名称 */
    @Column(length = 128, columnDefinition="varchar(128) DEFAULT NULL COMMENT '完整的层次名称'")
    private String fullPostName;

    /** 完整的层级Id */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '完整的层级Id'")
    private String fullPostParent;

    /** 完整的层级编号 */
    @Column(length = 100, columnDefinition="varchar(100) DEFAULT NULL COMMENT '完整的层级编号'")
    private String fullPostParentCode;

    /** 备注描述 */
    @Column(length = 100,  columnDefinition="varchar(100) DEFAULT NULL COMMENT '备注描述'")
    private String postDescription;

    /** 预留字段1 */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @Column(length = 65, columnDefinition="varchar(65) DEFAULT NULL COMMENT '预留字段2'")
    private String attributeTwo;
}

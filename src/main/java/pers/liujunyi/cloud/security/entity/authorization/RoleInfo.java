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
 * 文件名称: RoleInfo.java
 * 文件描述: 角色信息
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
@Document(collection = "roleInfo")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "role_info", comment = "角色表")
public class RoleInfo extends BaseEntity {


    /** 角色编号 */
    @Column(length = 15, nullable = false, columnDefinition="varchar(15) NOT NULL COMMENT '角色编号'")
    private String roleNumber;

    /** 角色名称 */
    @Column(length = 32,  columnDefinition="varchar(32) NOT NULL COMMENT '角色名称'")
    private String roleName;

    /** 父级主键id */
    @Column(columnDefinition="bigint(20) DEFAULT '0' COMMENT '父级主键id'")
    @Indexed
    private Long parentId;

    /** 完整的层级ID */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '完整的层级ID'")
    private String fullRoleParent;

    /** 完整的层级编号 */
    @Column(length = 100, columnDefinition="varchar(100) DEFAULT NULL COMMENT '完整的层级编号'")
    private String fullRoleParentCode;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte roleStatus;

    /** 角色授权代码 授权代码需要加ROLE_前缀 */
    @Column(length = 15,  columnDefinition="varchar(15) DEFAULT NULL COMMENT '角色授权代码 授权代码需要加ROLE_前缀'")
    private String roleAuthorizationCode;

    /** 备注描述 */
    @Column(length = 50,  columnDefinition="varchar(100) DEFAULT NULL COMMENT '备注描述'")
    private String roleDescription;
}

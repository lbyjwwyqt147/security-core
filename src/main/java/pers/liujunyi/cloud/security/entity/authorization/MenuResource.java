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
 * 文件名称: MenuResource.java
 * 文件描述: 菜单资源
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
@Document(collection = "menuResource")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "menu_resource", comment = "菜单资源表")
public class MenuResource extends BaseEntity {

    /** 菜单资源编号 */
    @Column(length = 15, nullable = false, columnDefinition="varchar(15) NOT NULL COMMENT '菜单资源编号'")
    private String menuNumber;

    /** 菜单资源名称 */
    @Column(length = 32,  columnDefinition="varchar(32) NOT NULL COMMENT '菜单资源名称'")
    private String menuName;

    /** 资源类型 1:目录  2：菜单界面   3：功能按钮 */
    @Column(columnDefinition="tinyint(4) NOT NULL COMMENT '资源类型 1:目录  2：菜单界面   3：功能按钮 '")
    @Indexed
    private Byte menuClassify;

    /** 菜单资源图标 */
    @Column(length = 32,  columnDefinition="varchar(32) DEFAULT NULL COMMENT '菜单资源图标'")
    private String menuIcon;

    /** 菜单资源路径 */
    @Column(columnDefinition="varchar(255) DEFAULT NULL COMMENT '菜单资源路径'")
    private String menuPath;

    /** 排序值 */
    @Column(columnDefinition="tinyint(4) DEFAULT '10' COMMENT '排序值'")
    private Byte serialNumber;

    /** 层级 */
    @Column(columnDefinition="tinyint(4) DEFAULT '1' COMMENT '层级'")
    private Byte menuLevel;

    /** 父级主键id */
    @Column(columnDefinition="bigint(20) DEFAULT '0' COMMENT '父级主键id'")
    @Indexed
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte menuStatus;

    /** 权限授权代码  授权代码不需要加ROLE_前缀 */
    @Column(length = 15,  columnDefinition="varchar(15) DEFAULT NULL COMMENT '权限授权代码  授权代码不需要加ROLE_前缀'")
    private String menuAuthorizationCode;

    /** 完整的层次名称 */
    @Column(length = 128, columnDefinition="varchar(128) DEFAULT NULL COMMENT '完整的层次名称'")
    private String fullMenuName;

    /** 完整的层级Id */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '完整的层级Id'")
    private String fullMenuParent;

    /** 完整的层级代码 */
    @Column(length = 100, columnDefinition="varchar(100) DEFAULT NULL COMMENT '完整的层级代码'")
    private String fullMenuParentCode;

    /** 备注描述 */
    @Column(length = 100,  columnDefinition="varchar(100) DEFAULT NULL COMMENT '备注描述'")
    private String menuDescription;

    /** 预留字段1 */
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @Column(length = 65, columnDefinition="varchar(65) DEFAULT NULL COMMENT '预留字段2'")
    private String attributeTwo;
}

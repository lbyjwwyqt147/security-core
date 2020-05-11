package pers.liujunyi.cloud.security.entity.authorization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pers.liujunyi.cloud.common.annotation.CustomerField;
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
@Entity(name = "MenuResource")
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "menu_resource")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "menu_resource", comment = "菜单资源表")
public class MenuResource extends BaseEntity {

    /** 菜单资源编号 */
    @CustomerField(isLog = false)
    @Column(length = 15, nullable = false, columnDefinition="varchar(15) NOT NULL COMMENT '菜单资源编号'")
    private String menuNumber;

    /** 菜单资源名称 */
    @CustomerField(desc = "资源名称")
    @Column(length = 32,  columnDefinition="varchar(32) NOT NULL COMMENT '菜单资源名称'")
    private String menuName;

    /** 资源类型 1:目录  2：菜单界面   3：功能按钮 */
    @CustomerField(desc = "资源类型 1:目录  2：菜单界面   3：功能按钮")
    @Column(columnDefinition="tinyint(4) NOT NULL COMMENT '资源类型 1:目录  2：菜单界面   3：功能按钮 '")
    @Indexed
    private Byte menuClassify;

    /** 菜单资源图标 */
    @CustomerField(desc = "资源图标")
    @Column(length = 32,  columnDefinition="varchar(32) DEFAULT NULL COMMENT '菜单资源图标'")
    private String menuIcon;

    /** 菜单资源路径 */
    @CustomerField(desc = "资源路径")
    @Column(columnDefinition="varchar(255) DEFAULT NULL COMMENT '菜单资源路径'")
    private String menuPath;

    /** 排序值 */
    @CustomerField(desc = "排序值")
    @Column(columnDefinition="tinyint(4) DEFAULT '10' COMMENT '排序值'")
    private Byte serialNumber;

    /** 层级 */
    @CustomerField(isLog = false)
    @Column(columnDefinition="tinyint(4) DEFAULT '1' COMMENT '层级'")
    private Byte menuLevel;

    /** 父级主键id */
    @CustomerField(isLog = false)
    @Column(columnDefinition="bigint(20) DEFAULT '0' COMMENT '父级主键id'")
    @Indexed
    private Long parentId;

    /** 状态：0：正常  1：禁用 */
    @CustomerField(desc = "状态：0：正常  1：禁用")
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte menuStatus;

    /** 权限授权代码  授权代码不需要加ROLE_前缀 */
    @CustomerField(desc = "权限授权代码")
    @Column(length = 15,  columnDefinition="varchar(15) DEFAULT NULL COMMENT '权限授权代码  授权代码不需要加ROLE_前缀'")
    private String menuAuthorizationCode;

    /** 按钮类别 */
    @CustomerField(desc = "按钮类别：1：保存  2：删除 3：修改 7：导入 8：导出 10：同步")
    @Column(columnDefinition="tinyint(4) DEFAULT NULL COMMENT '按钮类别：1：保存  2：删除 3：修改 7：导入 8：导出 10：同步 '")
    private Byte buttonCategory;

    /** 完整的层级Id */
    @CustomerField(isLog = false)
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '完整的层级Id'")
    private String fullMenuParent;

    /** 完整的层级编号 */
    @CustomerField(isLog = false)
    @Column(length = 100, columnDefinition="varchar(100) DEFAULT NULL COMMENT '完整的层级编号'")
    private String fullMenuParentCode;

    /** 备注描述 */
    @CustomerField(desc = "备注描述")
    @Column(length = 50,  columnDefinition="varchar(100) DEFAULT NULL COMMENT '备注描述'")
    private String menuDescription;

    /** 预留字段1 */
    @CustomerField()
    @Column(length = 45, columnDefinition="varchar(45) DEFAULT NULL COMMENT '预留字段1'")
    private String attributeOne;

    /** 预留字段2 */
    @CustomerField()
    @Column(length = 65, columnDefinition="varchar(65) DEFAULT NULL COMMENT '预留字段2'")
    private String attributeTwo;
}

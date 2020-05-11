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
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/***
 * 文件名称: RoleResource.java
 * 文件描述: 角色资源
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity(name = "RoleResource")
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "role_resource")
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "role_resource", comment = "角色资源表")
public class RoleResource extends BaseEntity {

    /** 角色id */
    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '角色ID'")
    @Indexed
    private Long roleId;

    /** 资源id */
    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '资源id'")
    @Indexed
    private Long resourceId;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte status;
}

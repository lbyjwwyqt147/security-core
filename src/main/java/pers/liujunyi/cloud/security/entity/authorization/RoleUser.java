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
 * 文件名称: RoleUser.java
 * 文件描述: 用户角色
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
@Document(collection = "roleUser")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "role_user", comment = "用户角色表")
public class RoleUser extends BaseEntity {

    /** 角色id */
    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '角色ID'")
    @Indexed
    private Long roleId;

    /** 用户id */
    @Column(columnDefinition="bigint(20) NOT NULL COMMENT '用户id'")
    @Indexed
    private Long userId;

    /** 状态：0：正常  1：禁用 */
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：禁用 '")
    @Indexed
    private Byte status;

}

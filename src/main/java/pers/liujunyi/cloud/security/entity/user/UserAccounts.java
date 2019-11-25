package pers.liujunyi.cloud.security.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
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
import javax.persistence.Version;
import java.util.Date;


/***
 * 文件名称: UserAccounts.java
 * 文件描述: 用户帐号信息
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
@Document(collection = "userAccounts")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "user_accounts", comment = "用户账户表")
public class UserAccounts extends BaseEntity {

    /** 用户帐号 */
    @Column(length = 65, nullable = false, columnDefinition="COMMENT '用户帐号'")
    @Indexed
    private String userAccounts;

    /** 用户编号  */
    @Column(length = 20, nullable = false, columnDefinition="COMMENT '用户编号'")
    @Indexed
    private String userNumber;

    /** 用户名称 */
    @Column(length = 32,  columnDefinition="COMMENT '用户名称'")
    private String userName;

    /** 用户昵称 */
    @Column(length = 32, nullable = false, columnDefinition="COMMENT '用户昵称'")
    private String userNickName;

    /** 用户密码 */
    @JSONField(serialize = false)
    @Column(length = 128, nullable = false, columnDefinition="COMMENT '用户密码'")
    private String userPassword;

    /** 绑定的手机号 */
    @Column(length = 11, nullable = false, columnDefinition="COMMENT '绑定的手机号'")
    @Indexed
    private String mobilePhone;

    /** 电子邮箱 */
    @Column(length = 65, columnDefinition="COMMENT '电子邮箱'")
    private String userMailbox;

    /** 状态：0：正常  1：冻结 */
    @Column(columnDefinition="COMMENT '状态：0：正常  1：冻结 '")
    @Indexed
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    @Column(columnDefinition="COMMENT '用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客'")
    private Byte userCategory;

    /** 注册时间  */
    @Column(columnDefinition="timestamp NOT NULL COMMENT '注册时间'")
    private Date registrationTime;

    /** 最后修改密码时间 */
    @Column(columnDefinition="timestamp COMMENT '最后修改密码时间'")
    private Date changePasswordTime;

    /** 头像地址 */
    @Column(columnDefinition="COMMENT '头像地址'")
    private String portrait;

    /** 头像ID */
    @Column(columnDefinition="COMMENT '头像ID'")
    private Long portraitId;

    @Version
    @Override
    public Long getDataVersion() {
        return super.getDataVersion();
    }
}
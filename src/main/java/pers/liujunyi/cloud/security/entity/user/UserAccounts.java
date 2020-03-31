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
import pers.liujunyi.cloud.common.annotation.CustomerField;
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
@Document(collection = "user_accounts")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Table(appliesTo = "user_accounts", comment = "用户账户表")
public class UserAccounts extends BaseEntity {

    /** 用户帐号 */
    @CustomerField(desc = "帐号")
    @Column(length = 65, nullable = false, columnDefinition="varchar(65) NOT NULL COMMENT '用户帐号'")
    @Indexed
    private String userAccounts;

    /** 用户编号  */
    @CustomerField(desc = "编号")
    @Column(length = 20, nullable = false, columnDefinition="varchar(20) NOT NULL COMMENT '用户编号'")
    @Indexed
    private String userNumber;

    /** 用户名称 */
    @CustomerField(desc = "名称")
    @Column(length = 32,  columnDefinition="varchar(32) DEFAULT NULL COMMENT '用户名称'")
    private String userName;

    /** 用户昵称 */
    @CustomerField(desc = "昵称")
    @Column(length = 32, nullable = false, columnDefinition="varchar(32) NOT NULL COMMENT '用户昵称'")
    private String userNickName;

    /** 用户密码 */
    @CustomerField(desc = "密码")
    @JSONField(serialize = false)
    @Column(length = 128, nullable = false, columnDefinition="varchar(128) NOT NULL COMMENT '用户密码'")
    private String userPassword;

    /** 绑定的手机号 */
    @CustomerField(desc = "绑定的手机号")
    @Column(length = 11, nullable = false, columnDefinition="varchar(11) NOT NULL COMMENT '绑定的手机号'")
    @Indexed
    private String mobilePhone;

    /** 电子邮箱 */
    @CustomerField(desc = "电子邮箱")
    @Column(length = 65, columnDefinition="varchar(65) DEFAULT NULL COMMENT '电子邮箱'")
    private String userMailbox;

    /** 状态：0：正常  1：冻结 */
    @CustomerField(desc = "状态：0：正常  1：冻结")
    @Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '状态：0：正常  1：冻结 '")
    @Indexed
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    @CustomerField(desc = "用户类别")
    @Column(columnDefinition="tinyint(4) DEFAULT NULL COMMENT '用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客'")
    private Byte userCategory;

    /** 注册时间  */
    @CustomerField(desc = "注册时间")
    @Column(columnDefinition="timestamp NOT NULL COMMENT '注册时间'")
    private Date registrationTime;

    /** 最后修改密码时间 */
    @CustomerField(desc = "最后修改密码时间")
    @Column(columnDefinition="timestamp DEFAULT NULL COMMENT '最后修改密码时间'")
    private Date changePasswordTime;

    /** 头像地址 */
    @CustomerField(desc = "头像地址")
    @Column(columnDefinition="varchar(255) DEFAULT NULL COMMENT '头像地址'")
    private String portrait;

    /** 头像ID */
    @Column(columnDefinition="bigint(20) DEFAULT NULL COMMENT '头像ID'")
    private Long portraitId;


    /** 最近登录时间 */
    @CustomerField(desc = "最近登录时间")
    @Column(columnDefinition="timestamp DEFAULT NULL COMMENT '最近登录时间'")
    private Date loginTime;

    /** 上次登录时间 */
    @CustomerField(desc = "上次登录时间")
    @Column(columnDefinition="timestamp DEFAULT NULL COMMENT '上次登录时间'")
    private Date lastLoginTime;

    /** 登录次数 */
    @CustomerField(desc = "登录次数")
    @Column(columnDefinition="int(11) DEFAULT '0' COMMENT '登录次数'")
    private Integer loginCount;

    @Version
    @Override
    public Long getDataVersion() {
        return super.getDataVersion();
    }
}
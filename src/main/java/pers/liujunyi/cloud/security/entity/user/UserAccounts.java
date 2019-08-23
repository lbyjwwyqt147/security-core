package pers.liujunyi.cloud.security.entity.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "photo_manage_user_accounts", type = "userAccounts", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class UserAccounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户帐号 */
    private String userAccounts;

    /** 用户编号  */
    private String userNumber;

    /** 用户名称 */
    private String userName;

    /** 用户昵称 */
    private String userNickName;

    /** 用户密码 */
    @JSONField(serialize = false)
    private String userPassword;

    /** 绑定的手机号 */
    private String mobilePhone;

    /** 电子邮箱 */
    private String userMailbox;

    /** 状态：0：正常  1：冻结 */
    private Byte userStatus;

    /** 用户类别   0：超级管理员 1：普通管理员  2：员工  3：顾客 */
    private Byte userCategory;

    /** 注册时间  */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date registrationTime;

    /** 最后修改密码时间 */
    @Field(type = FieldType.Date, index = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date changePasswordTime;

    /** 修改时间  */
    @Field(type = FieldType.Date, index = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    @Version
    private Long dataVersion;

    private Long lessee;
}
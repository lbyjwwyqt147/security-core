package pers.liujunyi.cloud.security.domain.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author ljy
 */
@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = -1123740072314967961L;

    @NotNull(message = "用户名不能为空")
    private String userAccount;

    @NotNull(message = "密码不能为空")
    private String userPassword;

    /** 客户端  1 或者 空：ajax 请求   -1: form表单提交  */
    private Byte client;
}

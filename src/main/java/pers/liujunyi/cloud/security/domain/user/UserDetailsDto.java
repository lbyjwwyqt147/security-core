package pers.liujunyi.cloud.security.domain.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liujunyi.cloud.common.vo.user.UserDetails;

/**
 * @author ljy
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailsDto extends UserDetails {

    private static final long serialVersionUID = 1257771469759843915L;
    /** 人员权限信息 */
    private String authorities;
    /** flase 未认证   true 认证通过 */
    private Boolean authenticated;
    private Object credentials;
    private Object details;
    private Object principal;
    private String token;
    private String secret;
}

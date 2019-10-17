package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import pers.liujunyi.cloud.common.exception.DescribeException;

/***
 * 文件名称: CustomWebResponseExceptionTranslator.java
 * 文件描述:  登录异常认证处理
 * 公 司:
 * 内容摘要:
 * 其他说明:
 *        对于登录认证异常处理配置，然后是处理类，通过WebResponseExceptionTranslator转为自定义的OAuth2Exception，然后使用jackson定制化类的json序列化，达到自定义异常信息的目的
 *
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
public class CustomWebResponseExceptionTranslator  implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception exception) throws Exception {
        if (exception instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
            log.info(oAuth2Exception.getMessage());
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new DescribeException(oAuth2Exception.getMessage()));
        }else if(exception instanceof AuthenticationException){
            AuthenticationException authenticationException = (AuthenticationException) exception;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new DescribeException(authenticationException.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new DescribeException(exception.getMessage()));
    }

}

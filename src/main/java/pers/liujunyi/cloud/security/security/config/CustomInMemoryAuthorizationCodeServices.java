package pers.liujunyi.cloud.security.security.config;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

/***
 * 文件名称: CustomInMemoryAuthorizationCodeServices
 * 文件描述:  自定义 code 生成规则
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2020/5/18 15:56
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public class CustomInMemoryAuthorizationCodeServices extends InMemoryAuthorizationCodeServices {

    private RandomValueStringGenerator generator = new RandomValueStringGenerator();

    public CustomInMemoryAuthorizationCodeServices() {
        this.generator = new RandomValueStringGenerator(32);
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        store(code, authentication);
        return code;
    }

}

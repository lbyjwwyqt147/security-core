package pers.liujunyi.cloud.security.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 常量信息
 * @author ljy
 */
public class SecurityConstant {
    /** 禁用状态 */
    public static final Byte DISABLE_STATUS = 1;
    /** 启用状态 */
    public static final Byte ENABLE_STATUS = 0;
    public static final String DATA_GRID_MESSAGE = "无数据";
    public static final String RESOURCE_ID = "resource_id";
    /** 客户端 */
    public static final String CLIEN_ID = "cloud_centre";
    /** secret客户端安全码 */
    public static final String CLIENT_SECRET = "secret";
    /** 客户端的access_token的有效时间值(单位:秒) 这里设置为 24小时(1天) */
    public static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 60*60*24;
    /** 客户端的refresh_token的有效时间值(单位:秒) 这里设置为 24小时(1天) */
    public static final Integer REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24;

    /** 资源类型：1 目录 2：界面  3：按钮 */
    public static final Byte RESOURCE_DIRECTORY = 1;
    public static final Byte RESOURCE_TECHWEB = 2;
    public static final Byte RESOURCE_BUTTON = 3;

    /** 权限就是那些以ROLE_为前缀的角色 */
    public static final String ROLE_PREFIX = "ROLE_";
    /** OAuth2 客户端需要的角色 */
    public static final String RESOURCE_AUTHORITIES = "ROLE_CLIENT_SIDE";

    /**
     * 数据状态值
     * @param status
     * @return
     */
    public static String getStatusValue(Byte status) {
        String statusValue = null;
        switch (status.byteValue()) {
            case 1:
                statusValue = "禁用";
                break;
            case 0:
                statusValue = "正常";
                break;
            default:
                statusValue = "正常";
                break;
        }
        return statusValue;
    }

    /**
     * 权限字符串转换位对象
     * @param auths
     * @return
     */
    public static Set<GrantedAuthority> grantedAuths(String auths) {
        Set<GrantedAuthority> authoritySet = new HashSet<>();
       if (StringUtils.isNotBlank(auths)) {
           JSONArray jsonArray = JSON.parseArray(auths);
           for (Object object : jsonArray) {
               JSONObject jsonObject = (JSONObject) object;
               authoritySet.add(new SimpleGrantedAuthority(jsonObject.getString("authority")));
           }
       }
       return authoritySet;
    }

    /**
     * 不需要保护的资源
     * @param excludeAntMatchers
     * @return
     */
    public static String[] antMatchers(String excludeAntMatchers) {
        String[] tempAntMatchers = excludeAntMatchers.trim().split(",");
        int length =  tempAntMatchers.length;
        String[] matchers = new String[length];
        for (int i = 0; i < length; i++) {
            matchers[i] = tempAntMatchers[i].trim();
        }
        return matchers;
    }

    /**
     * 不需要保护的资源
     * @param excludeAntMatchers
     * @return
     */
    public static String[] antMatchers(String[] excludeAntMatchers) {
        List<String> collect =  Arrays.asList(excludeAntMatchers).stream().map(String::trim).collect(Collectors.toList());
        return collect.toArray(new String[collect.size()]);
    }

    /**
     * 不需要保护的资源
     * @param excludeAntMatchers
     * @return
     */
    public static String[] antMatchers(List<String> excludeAntMatchers) {
        List<String> collect =  excludeAntMatchers.stream().map(String::trim).collect(Collectors.toList());
        return collect.toArray(new String[collect.size()]);
    }

}

package pers.liujunyi.cloud.security.security.config;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 *
 * @author ljy
 */
public class AntUrlPathMatcher {

    private boolean requiresLowerCaseUrl;
    private static PathMatcher pathMatcher;
    public AntUrlPathMatcher()   {
        this(true);

    }
    public AntUrlPathMatcher(boolean requiresLowerCaseUrl) {
        this.requiresLowerCaseUrl = true;
        this.pathMatcher = new AntPathMatcher();
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

    public Object compile(String path) {
        if (this.requiresLowerCaseUrl) {
            return path.toLowerCase();
        }
        return path;
    }

    public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl){

        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

    public static boolean pathMatchesUrl(Object path, String url) {
        if (("/**".equals(path)) || ("**".equals(path))) {
            return true;
        }
        return pathMatcher.match((String)path, url);
    }

    public String getUniversalMatchPattern() {
        return"/**";
    }

    public boolean requiresLowerCaseUrl() {
        return this.requiresLowerCaseUrl;
    }

}

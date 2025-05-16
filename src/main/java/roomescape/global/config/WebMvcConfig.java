package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.controller.interceptor.AdminAuthInterceptor;
import roomescape.auth.controller.interceptor.LoginRequiredInterceptor;
import roomescape.auth.controller.resolver.LoginMemberIdArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String ALL_PATH_PATTERN = "/**";
    private static final String CSS_PATH_PATTERN = "/css/**";
    private static final String IMAGE_PATH_PATTERN = "/image/**";
    private static final String JS_PATH_PATTERN = "/js/**";


    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;
    private final AdminAuthInterceptor adminAuthInterceptor;
    private final LoginRequiredInterceptor loginRequiredInterceptor;

    public WebMvcConfig(LoginMemberIdArgumentResolver loginMemberIdArgumentResolver,
                        AdminAuthInterceptor adminAuthInterceptor, LoginRequiredInterceptor loginRequiredInterceptor) {
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
        this.adminAuthInterceptor = adminAuthInterceptor;
        this.loginRequiredInterceptor = loginRequiredInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns(ALL_PATH_PATTERN)
                .excludePathPatterns(CSS_PATH_PATTERN, IMAGE_PATH_PATTERN, JS_PATH_PATTERN);

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns(ALL_PATH_PATTERN)
                .excludePathPatterns(CSS_PATH_PATTERN, IMAGE_PATH_PATTERN, JS_PATH_PATTERN);
    }

}

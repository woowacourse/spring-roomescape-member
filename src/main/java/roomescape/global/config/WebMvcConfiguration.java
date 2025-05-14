package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.interceptor.AdminAuthorizationInterceptor;
import roomescape.global.resolver.LoginMemberIdArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;

    public WebMvcConfiguration(LoginMemberIdArgumentResolver loginMemberIdArgumentResolver,
                               AdminAuthorizationInterceptor adminAuthorizationInterceptor) {
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
        this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorizationInterceptor)
                .addPathPatterns("/admin/**");
    }
}

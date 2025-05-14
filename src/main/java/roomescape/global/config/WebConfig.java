package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.auth.interceptor.AdminRoleCheckInterceptor;
import roomescape.global.auth.resolver.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminRoleCheckInterceptor adminRoleCheckInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                     AdminRoleCheckInterceptor adminRoleCheckInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminRoleCheckInterceptor = adminRoleCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminRoleCheckInterceptor)
                .addPathPatterns("/admin/**");
    }
}

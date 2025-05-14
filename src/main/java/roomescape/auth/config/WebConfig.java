package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.domain.AdminAuthorizationInterceptor;
import roomescape.auth.domain.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(final AdminAuthorizationInterceptor adminAuthorizationInterceptor,
                     final LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorizationInterceptor)
                .addPathPatterns("/admin");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}

package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.argumentresolver.LoginMemberArgumentResolver;
import roomescape.controller.interceptor.AdminAuthorizationInterceptor;
import roomescape.controller.interceptor.UserAuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final UserAuthorizationInterceptor userAuthorizationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(AdminAuthorizationInterceptor adminAuthorizationInterceptor,
                     UserAuthorizationInterceptor userAuthorizationInterceptor,
                     LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
        this.userAuthorizationInterceptor = userAuthorizationInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorizationInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(userAuthorizationInterceptor).addPathPatterns("/reservation");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}

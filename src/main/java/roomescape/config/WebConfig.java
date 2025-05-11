package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.argumentResolver.LoginMemberArgumentResolver;
import roomescape.config.interceptor.AdminRoleInterceptor;
import roomescape.config.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AuthInterceptor authInterceptor;
    private final AdminRoleInterceptor adminRoleInterceptor;

    public WebConfig(final LoginMemberArgumentResolver loginMemberArgumentResolver,
                     final AuthInterceptor authInterceptor,
                     final AdminRoleInterceptor adminRoleInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.authInterceptor = authInterceptor;
        this.adminRoleInterceptor = adminRoleInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**",
                        "/", "/login", "/signup");

        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns("/**");
    }
}

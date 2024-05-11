package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminRoleInterceptor;
import roomescape.auth.LoginMemberIdArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AdminRoleInterceptor adminRoleInterceptor;
    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;

    public WebConfig(AdminRoleInterceptor adminRoleInterceptor,
                     LoginMemberIdArgumentResolver loginMemberIdArgumentResolver) {
        this.adminRoleInterceptor = adminRoleInterceptor;
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns("/admin/**");
    }
}

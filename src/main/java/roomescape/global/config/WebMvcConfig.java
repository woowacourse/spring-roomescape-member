package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.controller.interceptor.AdminAuthInterceptor;
import roomescape.auth.controller.resolver.LoginMemberIdArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;
    private final AdminAuthInterceptor adminAuthInterceptor;

    public WebMvcConfig(LoginMemberIdArgumentResolver loginMemberIdArgumentResolver,
                        AdminAuthInterceptor adminAuthInterceptor) {
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**");
    }

}

package roomescape.config;


import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.AdminHandlerInterceptor;
import roomescape.controller.LoginMemberPrincipalArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberPrincipalArgumentResolver loginMemberPrincipalArgumentResolver;
    private final AdminHandlerInterceptor adminHandlerInterceptor;

    public WebMvcConfiguration(LoginMemberPrincipalArgumentResolver loginMemberPrincipalArgumentResolver,
                               AdminHandlerInterceptor adminHandlerInterceptor) {
        this.loginMemberPrincipalArgumentResolver = loginMemberPrincipalArgumentResolver;
        this.adminHandlerInterceptor = adminHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberPrincipalArgumentResolver);
    }
}


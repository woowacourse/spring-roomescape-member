package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.service.AuthService;
import roomescape.presentation.argumentresolver.MemberArgumentResolver;
import roomescape.presentation.interceptor.AdminAuthorizationInterceptor;
import roomescape.presentation.interceptor.MemberAuthenticationInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public WebConfig(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MemberArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new MemberAuthenticationInterceptor(authService))
                .addPathPatterns("/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/signup/**")
                .excludePathPatterns("/");

        registry.addInterceptor(new AdminAuthorizationInterceptor())
                .addPathPatterns("/admin/**");
    }
}

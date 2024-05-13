package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.AuthService;
import roomescape.ui.interceptor.AuthenticationExtractInterceptor;
import roomescape.ui.interceptor.CheckAdminAccessInterceptor;
import roomescape.ui.support.AuthenticationPrincipalArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public WebMvcConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationExtractInterceptor(authService))
                .excludePathPatterns("/", "/login", "/logout", "/themes/popular")
                .excludePathPatterns("/css/**", "/js/**", "/image/**");
        registry.addInterceptor(new CheckAdminAccessInterceptor()).addPathPatterns("/admin/**");
    }
}

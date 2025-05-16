package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infra.auth.AuthenticationPrincipalArgumentResolver;
import roomescape.infra.auth.CheckAdminInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public WebMvcConfiguration(CheckAdminInterceptor checkAdminInterceptor,
                               AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
    }
}

package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.handler.AdminAuthenticationInterceptor;
import roomescape.config.handler.AuthenticationArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AuthenticationArgumentResolver authenticationArgumentResolver;
    private final AdminAuthenticationInterceptor adminAuthenticationInterceptor;

    public WebMvcConfiguration(AuthenticationArgumentResolver authenticationArgumentResolver,
                               AdminAuthenticationInterceptor adminAuthenticationInterceptor) {
        this.authenticationArgumentResolver = authenticationArgumentResolver;
        this.adminAuthenticationInterceptor = adminAuthenticationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthenticationInterceptor)
                .addPathPatterns("/admin/**");
    }
}

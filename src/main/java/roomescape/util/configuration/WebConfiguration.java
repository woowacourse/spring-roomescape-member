package roomescape.util.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.auth.AuthService;
import roomescape.util.infrastructure.AuthenticationPrincipalArgumentResolver;
import roomescape.util.infrastructure.RoleCheckInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final AuthService authService;

    public WebConfiguration(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RoleCheckInterceptor(authService))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService));
    }
}

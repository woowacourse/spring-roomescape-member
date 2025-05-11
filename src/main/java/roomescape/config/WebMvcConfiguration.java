package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.auth.AuthService;
import roomescape.infrastructure.AuthenticationPrincipalArgumentResolver;
import roomescape.infrastructure.AuthenticationPrincipalExtractor;
import roomescape.infrastructure.CheckAdminRoleInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthenticationPrincipalExtractor authenticationPrincipalExtractor;

    public WebMvcConfiguration(
            AuthService authService,
            AuthenticationPrincipalExtractor authenticationPrincipalExtractor
    ) {
        this.authService = authService;
        this.authenticationPrincipalExtractor = authenticationPrincipalExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(
                new AuthenticationPrincipalArgumentResolver(authenticationPrincipalExtractor)
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                        new CheckAdminRoleInterceptor(authenticationPrincipalExtractor, authService))
                .addPathPatterns("/admin/**");
    }
}

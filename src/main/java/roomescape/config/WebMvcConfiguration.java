package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.presentation.AdminAuthorizationInterceptor;
import roomescape.presentation.AuthenticatedUserArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;

    public WebMvcConfiguration(
            final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver,
            final AdminAuthorizationInterceptor adminAuthorizationInterceptor
    ) {
        this.authenticatedUserArgumentResolver = authenticatedUserArgumentResolver;
        this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorizationInterceptor)
                .addPathPatterns("/admin/**");
    }
}

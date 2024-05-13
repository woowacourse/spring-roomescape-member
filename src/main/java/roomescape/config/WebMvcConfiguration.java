package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.AuthenticatedUserArgumentResolver;
import roomescape.controller.CheckAdminInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver, CheckAdminInterceptor checkAdminInterceptor) {
        this.authenticatedUserArgumentResolver = authenticatedUserArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
    }
}

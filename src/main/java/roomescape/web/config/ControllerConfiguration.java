package roomescape.web.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.web.support.AuthorizationArgumentResolver;
import roomescape.web.support.CheckAdminInterceptor;

@Configuration
public class ControllerConfiguration implements WebMvcConfigurer {

    private final AuthorizationArgumentResolver authorizationArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public ControllerConfiguration(AuthorizationArgumentResolver authorizationArgumentResolver,
                                   CheckAdminInterceptor checkAdminInterceptor) {
        this.authorizationArgumentResolver = authorizationArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.interceptor.RoleCheckInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver;
    private final RoleCheckInterceptor roleCheckInterceptor;

    public WebMvcConfig(HandlerMethodArgumentResolver handlerMethodArgumentResolver, RoleCheckInterceptor roleCheckInterceptor) {
        this.handlerMethodArgumentResolver = handlerMethodArgumentResolver;
        this.roleCheckInterceptor = roleCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(handlerMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(roleCheckInterceptor);
        interceptorRegistration.addPathPatterns("/admin/**");
    }
}

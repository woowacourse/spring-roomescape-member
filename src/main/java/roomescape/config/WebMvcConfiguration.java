package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final HandlerInterceptor handlerInterceptor;
    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver;

    public WebMvcConfiguration(final HandlerInterceptor handlerInterceptor,
                               final HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        this.handlerInterceptor = handlerInterceptor;
        this.handlerMethodArgumentResolver = handlerMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(handlerInterceptor);
        interceptorRegistration.addPathPatterns("/auth/login/check", "/user/reservations");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(handlerMethodArgumentResolver);
    }
}

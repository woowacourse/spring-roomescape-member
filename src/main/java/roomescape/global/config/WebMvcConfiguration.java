package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.interceptor.CheckAdminInterceptor;
import roomescape.global.interceptor.CheckLoginInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final CheckLoginInterceptor checkLoginInterceptor;
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver;

    public WebMvcConfiguration(final CheckLoginInterceptor checkLoginInterceptor,
                               final CheckAdminInterceptor checkAdminInterceptor,
                               final HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        this.checkLoginInterceptor = checkLoginInterceptor;
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.handlerMethodArgumentResolver = handlerMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/auth/login/check", "/user/**");
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(handlerMethodArgumentResolver);
    }
}

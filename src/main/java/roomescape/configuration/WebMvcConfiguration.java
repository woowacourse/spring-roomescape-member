package roomescape.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.configuration.interceptor.AuthHandlerInterceptor;
import roomescape.configuration.resolver.AccessTokenArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AccessTokenArgumentResolver accessTokenArgumentResolver;
    private final AuthHandlerInterceptor authHandlerInterceptor;

    public WebMvcConfiguration(
            AccessTokenArgumentResolver accessTokenArgumentResolver,
            AuthHandlerInterceptor authHandlerInterceptor
    ) {
        this.accessTokenArgumentResolver = accessTokenArgumentResolver;
        this.authHandlerInterceptor = authHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accessTokenArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor).addPathPatterns("/admin/**");
    }
}

package roomescape.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerMethodArgumentResolver loginMemberArgumentResolver;
    private final HandlerInterceptor roleCheckInterceptor;

    public WebMvcConfig(
            @Qualifier("loginMemberArgumentResolver") HandlerMethodArgumentResolver loginMemberArgumentResolver,
            @Qualifier("roleCheckInterceptor") HandlerInterceptor roleCheckInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.roleCheckInterceptor = roleCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(roleCheckInterceptor);
        interceptorRegistration.addPathPatterns("/admin/**");
    }
}

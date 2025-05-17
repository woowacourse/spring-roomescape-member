package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.interceptor.CheckLoginInterceptor;
import roomescape.auth.resolver.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckLoginInterceptor checkLoginInterceptor;

    public WebMvcConfiguration(LoginMemberArgumentResolver loginMemberArgumentResolver,
                               CheckLoginInterceptor checkLoginInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkLoginInterceptor = checkLoginInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/admin/**");
    }
}

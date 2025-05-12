package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.interceptor.CheckLoginInterceptor;
import roomescape.common.resolver.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CheckLoginInterceptor checkLoginInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfiguration(
            final CheckLoginInterceptor checkLoginInterceptor,
            final LoginMemberArgumentResolver loginMemberArgumentResolver
    ) {
        this.checkLoginInterceptor = checkLoginInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
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

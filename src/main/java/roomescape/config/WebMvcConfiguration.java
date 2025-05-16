package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.interceptor.CheckAdminInterceptor;
import roomescape.config.resolver.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(
            LoginMemberArgumentResolver loginMemberArgumentResolver,
            CheckAdminInterceptor checkAdminInterceptor
    ) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin")
                .addPathPatterns("/admin/*");
    }
}

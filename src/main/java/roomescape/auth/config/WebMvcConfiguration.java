package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.interceptor.CheckAdminInterceptor;
import roomescape.auth.interceptor.CheckLoginInterceptor;
import roomescape.auth.resolver.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckLoginInterceptor checkLoginInterceptor;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(LoginMemberArgumentResolver loginMemberArgumentResolver,
                               CheckLoginInterceptor checkLoginInterceptor,
                               CheckAdminInterceptor checkAdminInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkLoginInterceptor = checkLoginInterceptor;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/reservation")
                .addPathPatterns("/admin/**");

        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
    }
}

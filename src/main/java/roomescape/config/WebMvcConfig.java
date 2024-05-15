package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.Interceptor.CheckAdminInterceptor;
import roomescape.config.Interceptor.CheckLoginInterceptor;
import roomescape.config.resolver.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final CheckLoginInterceptor checkLoginInterceptor;

    public WebMvcConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                        CheckAdminInterceptor checkAdminInterceptor, CheckLoginInterceptor checkLoginInterceptor) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkLoginInterceptor = checkLoginInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/reservation/**", "/reservations/**");
    }
}

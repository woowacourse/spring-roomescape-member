package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.handler.CheckLoginInterceptor;
import roomescape.global.handler.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckLoginInterceptor checkLoginInterceptor;

    public WebConfig(
        LoginMemberArgumentResolver loginMemberArgumentResolver,
        CheckLoginInterceptor checkLoginInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkLoginInterceptor = checkLoginInterceptor;
    }

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/signup", "/admin/members");
    }
}

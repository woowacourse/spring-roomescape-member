package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.Interceptor.CheckAdminInterceptor;
import roomescape.config.resolver.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                        CheckAdminInterceptor checkAdminInterceptor) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

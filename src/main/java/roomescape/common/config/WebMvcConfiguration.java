package roomescape.common.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckLoginInterceptor checkLoginInterceptor;

    public WebMvcConfiguration(final LoginMemberArgumentResolver loginMemberArgumentResolver,
                               final CheckLoginInterceptor checkLoginInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkLoginInterceptor = checkLoginInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}

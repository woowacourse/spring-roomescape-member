package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.QueryStringArgumentResolver;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.LoginMemberArgumentResolver;
import roomescape.service.auth.VerifyAdminInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final QueryStringArgumentResolver queryStringArgumentResolver;

    public WebConfig(AuthService authService,
                     QueryStringArgumentResolver queryStringArgumentResolver) {
        this.authService = authService;
        this.queryStringArgumentResolver = queryStringArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // [질문] 일반적으로 ArgumentResolver 나 Interceptor 를 new 키워드로 생성하는지 빈으로 등록해서 사용하는지
        resolvers.add(new LoginMemberArgumentResolver(authService));
        resolvers.add(queryStringArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VerifyAdminInterceptor(authService)).addPathPatterns("/admin/**");
    }
}

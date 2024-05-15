package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.QueryStringArgumentResolver;
import roomescape.service.auth.LoginMemberArgumentResolver;
import roomescape.service.auth.VerifyAdminInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final QueryStringArgumentResolver queryStringArgumentResolver;
    private final VerifyAdminInterceptor verifyAdminInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                     QueryStringArgumentResolver queryStringArgumentResolver,
                     VerifyAdminInterceptor verifyAdminInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.queryStringArgumentResolver = queryStringArgumentResolver;
        this.verifyAdminInterceptor = verifyAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(queryStringArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyAdminInterceptor).addPathPatterns("/admin/**");
    }
}

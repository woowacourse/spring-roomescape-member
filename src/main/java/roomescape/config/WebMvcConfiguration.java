package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberTokenMethodArgumentResolver memberTokenMethodArgumentResolver;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(
            MemberTokenMethodArgumentResolver memberTokenMethodArgumentResolver,
            LoginMemberArgumentResolver loginMemberArgumentResolver,
            CheckAdminInterceptor checkAdminInterceptor
    ) {
        this.memberTokenMethodArgumentResolver = memberTokenMethodArgumentResolver;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberTokenMethodArgumentResolver);
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin")
                .addPathPatterns("/admin/*");
    }
}

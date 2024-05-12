package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminLoginInterceptor adminLoginInterceptor;
    private final MemberLoginInterceptor memberLoginInterceptor;

    public WebMvcConfiguration(final LoginMemberArgumentResolver loginMemberArgumentResolver, final AdminLoginInterceptor adminLoginInterceptor,
                               final MemberLoginInterceptor memberLoginInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminLoginInterceptor = adminLoginInterceptor;
        this.memberLoginInterceptor = memberLoginInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(memberLoginInterceptor).addPathPatterns("/reservation");
    }
}

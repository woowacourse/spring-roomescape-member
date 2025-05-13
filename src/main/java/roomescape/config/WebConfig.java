package roomescape.config;

import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.argumentResolver.LoginMemberArgumentResolver;
import roomescape.config.filter.LoginFilter;
import roomescape.config.interceptor.AdminRoleInterceptor;
import roomescape.jwt.JwtProvider;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminRoleInterceptor adminRoleInterceptor;

    public WebConfig(final LoginMemberArgumentResolver loginMemberArgumentResolver,
                     final AdminRoleInterceptor adminRoleInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminRoleInterceptor = adminRoleInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilterRegistrationBean(LoginFilter loginFilter) {
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loginFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public LoginFilter loginFilter(JwtProvider jwtProvider) {
        return new LoginFilter(jwtProvider);
    }
}

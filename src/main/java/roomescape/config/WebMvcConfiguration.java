package roomescape.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.controller.intercepter.CheckAdminLoginInterceptor;
import roomescape.controller.intercepter.CheckLoginInterceptor;
import roomescape.controller.resolver.LoginMemberArgumentResolver;
import roomescape.controller.resolver.ReservationDetailArgumentResolver;
import roomescape.service.TokenService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TokenService tokenService;

    public WebMvcConfiguration(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminLoginInterceptor(tokenService))
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new CheckLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**", "/logout", "/signup")
                .excludePathPatterns("/", "/themes/popular");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(tokenService));
        resolvers.add(new ReservationDetailArgumentResolver());
    }
}

package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.AuthService;
import roomescape.controller.infrastructure.AuthorizationExtractor;
import roomescape.controller.interceptor.CheckAdminInterceptor;
import roomescape.controller.login.AuthenticationPrincipalArgumentResolver;
import roomescape.controller.interceptor.CheckLoginInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public WebMvcConfiguration(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/signup").setViewName("/signup");
        registry.addViewController("/reservation").setViewName("/reservation");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor(authService, authorizationExtractor))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/error", "/login/**", "/signup",
                        "/members", "/themes/popular",
                        "/css/**", "/*.ico", "/js/**", "/image/**");

        registry.addInterceptor(new CheckAdminInterceptor(authService, authorizationExtractor))
                .order(2)
                .addPathPatterns("/admin/**", "/reservations/**", "/times/**", "/themes/**", "/members/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService, authorizationExtractor));
    }
}

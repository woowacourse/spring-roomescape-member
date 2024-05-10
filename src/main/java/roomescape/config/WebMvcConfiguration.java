package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.argumentresolver.AuthenticationPrincipalArgumentResolver;
import roomescape.controller.infrastructure.AuthenticationExtractor;
import roomescape.controller.interceptor.CheckAdminInterceptor;
import roomescape.controller.interceptor.CheckLoginInterceptor;
import roomescape.service.AuthService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthenticationExtractor authenticationExtractor;

    public WebMvcConfiguration(AuthService authService, AuthenticationExtractor authenticationExtractor) {
        this.authService = authService;
        this.authenticationExtractor = authenticationExtractor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/signup").setViewName("/signup");
        registry.addViewController("/reservation").setViewName("/reservation");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor(authService, authenticationExtractor))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/error", "/login/**", "/signup",
                        "/members", "/themes/popular",
                        "/css/**", "/*.ico", "/js/**", "/image/**");

        registry.addInterceptor(new CheckAdminInterceptor(authService, authenticationExtractor))
                .order(2)
                .addPathPatterns("/admin/**", "/reservations/**", "/times/**", "/themes/**", "/members/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService, authenticationExtractor));
    }
}

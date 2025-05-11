package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.infrastructure.AuthorizationExtractor;
import roomescape.auth.service.AuthService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public WebMvcConfig(final AuthService authService, final AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/reservation").setViewName("reservation");
        registry.addViewController("/signup").setViewName("signup");

        registry.addViewController("/admin").setViewName("admin/index");
        registry.addViewController("/admin/reservation").setViewName("admin/reservation-new");
        registry.addViewController("/admin/time").setViewName("admin/time");
        registry.addViewController("/admin/theme").setViewName("admin/theme");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService, authorizationExtractor));
    }
}

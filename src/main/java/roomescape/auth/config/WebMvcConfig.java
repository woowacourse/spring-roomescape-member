package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.service.AuthService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public WebMvcConfig(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authService));
    }
}

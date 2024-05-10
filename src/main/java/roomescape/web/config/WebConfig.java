package roomescape.web.config;


import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infrastructure.authentication.AuthService;
import roomescape.web.security.CookieTokenExtractor;
import roomescape.web.security.MemberIdArgumentResolver;

@Configuration
class WebConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public WebConfig(AuthService authService, CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberIdArgumentResolver(authService, extractor));
    }
}

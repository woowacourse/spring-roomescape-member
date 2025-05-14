package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.domain.AdminAuthorizationInterceptor;
import roomescape.auth.domain.CookieTokenExtractor;
import roomescape.auth.domain.LoginMemberArgumentResolver;
import roomescape.auth.service.AuthService;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public WebConfig(AuthService authService, CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthorizationInterceptor(authService, extractor))
                .addPathPatterns("/admin");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, extractor));
    }
}

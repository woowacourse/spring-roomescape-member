package roomescape.web.config;


import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.auth.AuthService;
import roomescape.web.security.AuthorizeAdminInterceptor;
import roomescape.web.security.CookieTokenExtractor;
import roomescape.web.security.MemberProfileArgumentResolver;

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
        resolvers.add(new MemberProfileArgumentResolver(authService, extractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizeAdminInterceptor(authService, extractor))
                .addPathPatterns("/admin/**");
    }
}

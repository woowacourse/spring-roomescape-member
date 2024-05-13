package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.helper.CookieExtractor;
import roomescape.controller.helper.LoginMemberArgumentResolver;
import roomescape.controller.helper.RoleAllowedInterceptor;
import roomescape.service.AuthService;

@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final CookieExtractor cookieExtractor;

    public AuthWebMvcConfig(AuthService authService, CookieExtractor cookieExtractor) {
        this.authService = authService;
        this.cookieExtractor = cookieExtractor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleAllowedInterceptor(authService, cookieExtractor));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, cookieExtractor));
    }
}

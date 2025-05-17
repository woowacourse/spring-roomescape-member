package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.service.AuthService;
import roomescape.global.auth.AdminCheckInterceptor;
import roomescape.global.auth.AuthCookie;
import roomescape.global.auth.MemberArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthCookie authCookie;

    public WebMvcConfiguration(final AuthService authService, final AuthCookie authCookie) {
        this.authService = authService;
        this.authCookie = authCookie;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authService, authCookie));
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor(authService, authCookie))
                .addPathPatterns("/admin/**");
    }
}

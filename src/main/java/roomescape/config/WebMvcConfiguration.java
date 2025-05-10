package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.service.MemberAuthService;
import roomescape.infrastructure.TokenCookieProvider;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final TokenCookieProvider tokenCookieProvider;
    private final MemberAuthService authService;

    public WebMvcConfiguration(TokenCookieProvider tokenCookieProvider, MemberAuthService authService) {
        this.tokenCookieProvider = tokenCookieProvider;
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberTokenMethodArgumentResolver(tokenCookieProvider));
        resolvers.add(new LoginMemberArgumentResolver(authService, tokenCookieProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(authService, tokenCookieProvider))
                .addPathPatterns("/admin")
                .addPathPatterns("/admin/*");
    }
}

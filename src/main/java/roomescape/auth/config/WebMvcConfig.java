package roomescape.auth.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.application.AuthService;
import roomescape.auth.domain.AuthTokenExtractor;
import roomescape.auth.domain.AuthTokenProvider;
import roomescape.auth.ui.CheckAuthRoleInterceptor;
import roomescape.auth.ui.MemberArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthTokenExtractor<String> authTokenExtractor;
    private final AuthTokenProvider authTokenProvider;
    private final AuthService authService;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(
                        new CheckAuthRoleInterceptor(authTokenExtractor, authTokenProvider)
                )
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberArgumentResolver(authTokenExtractor, authTokenProvider, authService));
    }
}

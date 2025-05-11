package roomescape.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.auth.AuthenticationInterceptor;
import roomescape.global.auth.AuthorizationArgumentResolver;
import roomescape.global.auth.CookieUtil;
import roomescape.global.auth.JwtUtil;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtUtil, cookieUtil))
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthorizationArgumentResolver(jwtUtil, cookieUtil));
    }
}

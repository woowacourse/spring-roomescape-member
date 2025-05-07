package roomescape.auth.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminRequestMatcher;
import roomescape.auth.AuthenticationPrincipalResolver;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.PrevRequestHandler;
import roomescape.auth.RequestMatcher;

@Configuration
@Import(AuthenticationConfig.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalResolver(jwtTokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PrevRequestHandler(jwtTokenProvider, requestMatchers()));
    }

    @Bean
    public List<RequestMatcher> requestMatchers() {
        return List.of(new AdminRequestMatcher());
    }
}

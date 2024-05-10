package roomescape.common;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.service.TokenProvider;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthorizationExtractor authorizationExtractor;
    private final TokenProvider tokenProvider;

    public WebMvcConfiguration(final AuthorizationExtractor authorizationExtractor, final TokenProvider tokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new AuthorizationInterceptor(authorizationExtractor, tokenProvider))
                .addPathPatterns("/admin/**");
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(tokenProvider, authorizationExtractor);
    }
}

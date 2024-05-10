package roomescape.common;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.TokenProvider;
import roomescape.auth.config.AuthenticationPrincipalArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;

    public WebMvcConfiguration(final TokenProvider tokenProvider) {
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
        registry.addInterceptor(new AuthenticationInterceptor(tokenProvider))
                .addPathPatterns("/admin/**");
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(tokenProvider);
    }
}

package roomescape.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.controller.resolver.AccessTokenArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AccessTokenArgumentResolver accessTokenArgumentResolver;

    public WebMvcConfiguration(AccessTokenArgumentResolver accessTokenArgumentResolver) {
        this.accessTokenArgumentResolver = accessTokenArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accessTokenArgumentResolver);
    }
}

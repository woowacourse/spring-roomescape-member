package roomescape.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.resolver.LoginMemberArgumentResolver;
import roomescape.service.TokenService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TokenService tokenService;

    public WebMvcConfiguration(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(tokenService));
    }
}

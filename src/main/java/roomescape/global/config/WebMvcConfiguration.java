package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.resolver.LoginMemberIdArgumentResolver;
import roomescape.global.security.JwtProvider;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;

    public WebMvcConfiguration(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(jwtProvider));
    }
}

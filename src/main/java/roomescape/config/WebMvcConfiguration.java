package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infrastructure.AuthenticationPrincipalArgumentResolver;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.infrastructure.TokenExtractor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfiguration(TokenExtractor tokenExtractor, JwtTokenProvider jwtTokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(tokenExtractor, jwtTokenProvider));
    }
}

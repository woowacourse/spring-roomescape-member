package roomescape.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.resolver.AuthenticationInformationArgumentResolver;
import roomescape.utility.JwtTokenProvider;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public WebConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationInformationArgumentResolver(jwtTokenProvider));
    }
}

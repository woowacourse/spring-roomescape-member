package roomescape.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.intercept.NotAdminBlockIntercept;
import roomescape.resolver.AuthenticationInformationArgumentResolver;
import roomescape.utility.CookieUtility;
import roomescape.utility.JwtTokenProvider;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtility cookieUtility;

    public WebConfiguration(JwtTokenProvider jwtTokenProvider, CookieUtility cookieUtility) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtility = cookieUtility;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationInformationArgumentResolver(jwtTokenProvider, cookieUtility));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NotAdminBlockIntercept(jwtTokenProvider, cookieUtility));
    }
}

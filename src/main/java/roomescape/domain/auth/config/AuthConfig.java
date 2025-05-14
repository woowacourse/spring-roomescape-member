package roomescape.domain.auth.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.domain.auth.service.AuthService;
import roomescape.domain.auth.service.JwtManager;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final JwtManager jwtManager;
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @Autowired
    public AuthConfig(final JwtManager jwtManager, final AuthService authService, final JwtProperties jwtProperties) {
        this.jwtManager = jwtManager;
        this.authService = authService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminLoginInterceptor(jwtManager, jwtProperties))
                .addPathPatterns( "/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TokenArgumentResolver(authService));
    }
}

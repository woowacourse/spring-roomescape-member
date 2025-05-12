package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import java.util.List;
import roomescape.common.auth.AdminInterceptor;
import roomescape.common.auth.JwtProvider;
import roomescape.common.auth.LoginCustomerResolver;
import roomescape.service.AuthService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    public WebConfig(AuthService authService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginCustomerResolver(authService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtProvider))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/login");
    }
}

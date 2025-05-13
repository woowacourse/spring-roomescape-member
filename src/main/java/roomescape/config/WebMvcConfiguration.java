package roomescape.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.interceptor.CheckLoginInterceptor;
import roomescape.auth.resolver.LoginMemberArgumentResolver;
import roomescape.auth.service.AuthService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;

    @Autowired
    public WebMvcConfiguration(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor(authService))
                .addPathPatterns("/admin/**");
    }
}

package roomescape.presentation;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.AuthenticationService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticationService authenticationService;

    public WebMvcConfiguration(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver(authenticationService));
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(authenticationService))
            .addPathPatterns("/admin/**");
    }
}

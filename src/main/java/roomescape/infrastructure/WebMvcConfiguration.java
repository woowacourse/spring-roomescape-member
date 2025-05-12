package roomescape.infrastructure;

import jakarta.inject.Inject;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final HandlerMethodArgumentResolver loginArgumentResolver;
    private final AdminAuthorityInterceptor adminAuthorityInterceptor;

    @Inject
    public WebMvcConfiguration(HandlerMethodArgumentResolver loginArgumentResolver,
                               AdminAuthorityInterceptor adminAuthorityInterceptor) {
        this.loginArgumentResolver = loginArgumentResolver;
        this.adminAuthorityInterceptor = adminAuthorityInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorityInterceptor)
                .addPathPatterns("/admin/**");
    }
}

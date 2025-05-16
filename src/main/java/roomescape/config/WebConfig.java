package roomescape.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infra.SessionLoginRepository;
import roomescape.interceptor.AdminAuthorizationInterceptor;
import roomescape.resolver.LoginInfoArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionLoginRepository sessionLoginRepository;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthorizationInterceptor(sessionLoginRepository))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginInfoArgumentResolver(sessionLoginRepository));
    }
}

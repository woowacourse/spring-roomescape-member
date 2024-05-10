package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthHandlerInterceptor;
import roomescape.auth.AuthenticatedMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedMemberArgumentResolver authenticatedMemberArgumentResolver;
    private final AuthHandlerInterceptor authHandlerInterceptor;

    public WebConfig(AuthenticatedMemberArgumentResolver authenticatedMemberArgumentResolver,
                     AuthHandlerInterceptor authHandlerInterceptor) {
        this.authenticatedMemberArgumentResolver = authenticatedMemberArgumentResolver;
        this.authHandlerInterceptor = authHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor).addPathPatterns("/admin/**");
    }
}

package roomescape.auth.infrastructure;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.infrastructure.argument.AuthorizedAdminArgumentResolver;
import roomescape.auth.infrastructure.argument.AuthorizedMemberArgumentResolver;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthorizedAdminArgumentResolver authorizedAdminArgumentResolver;
    private final AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver;

    public AuthConfig(
        AuthInterceptor authInterceptor,
        AuthorizedAdminArgumentResolver authorizedAdminArgumentResolver,
        AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver
    ) {
        this.authInterceptor = authInterceptor;
        this.authorizedAdminArgumentResolver = authorizedAdminArgumentResolver;
        this.authorizedMemberArgumentResolver = authorizedMemberArgumentResolver;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**");
    }

    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authorizedAdminArgumentResolver);
        argumentResolvers.add(authorizedMemberArgumentResolver);
    }
}

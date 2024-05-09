package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.resolver.AuthenticatedMemberPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedMemberPrincipalArgumentResolver authenticatedMemberPrincipalArgumentResolver;

    public WebConfig(final AuthenticatedMemberPrincipalArgumentResolver authenticatedMemberPrincipalArgumentResolver) {
        this.authenticatedMemberPrincipalArgumentResolver = authenticatedMemberPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberPrincipalArgumentResolver);
    }
}

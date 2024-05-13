package roomescape.config;

import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.auth.TokenManager;
import roomescape.presentation.auth.AdminRoleInterceptor;
import roomescape.presentation.auth.LoginMemberIdArgumentResolver;
import roomescape.presentation.auth.PermissionCheckInterceptor;
import roomescape.presentation.auth.CredentialContext;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenManager tokenManager;
    private final ObjectProvider<CredentialContext> payloadContextProvider;

    public WebConfig(TokenManager tokenManager, ObjectProvider<CredentialContext> payloadContextProvider) {
        this.tokenManager = tokenManager;
        this.payloadContextProvider = payloadContextProvider;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminRoleInterceptor(tokenManager, payloadContextProvider))
                .addPathPatterns("/admin/**");
        registry.addInterceptor(new PermissionCheckInterceptor(tokenManager, payloadContextProvider));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(tokenManager, payloadContextProvider));
    }
}

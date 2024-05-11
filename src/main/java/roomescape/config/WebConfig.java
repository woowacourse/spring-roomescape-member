package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminRoleInterceptor;
import roomescape.auth.LoginMemberIdArgumentResolver;
import roomescape.auth.PermissionCheckInterceptor;
import roomescape.auth.TokenManager;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenManager tokenManager;

    public WebConfig(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(tokenManager));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminRoleInterceptor(tokenManager))
                .addPathPatterns("/admin/**");
        registry.addInterceptor(new PermissionCheckInterceptor(tokenManager));
    }
}

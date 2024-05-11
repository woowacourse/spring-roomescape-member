package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminRoleInterceptor;
import roomescape.auth.LoginMemberIdArgumentResolver;
import roomescape.auth.TokenManager;
import roomescape.domain.role.RoleRepository;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenManager tokenManager;
    private final RoleRepository roleRepository;

    public WebConfig(TokenManager tokenManager, RoleRepository roleRepository) {
        this.tokenManager = tokenManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(tokenManager));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminRoleInterceptor(tokenManager, roleRepository))
                .addPathPatterns("/admin/**");
    }
}

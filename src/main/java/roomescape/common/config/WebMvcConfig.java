package roomescape.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.auth.AdminPageInterceptor;
import roomescape.member.auth.AuthorizationInterceptor;
import roomescape.member.auth.MemberInfoArgumentResolver;
import roomescape.member.service.AuthService;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberInfoArgumentResolver(authService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminPageInterceptor(authService))
                .addPathPatterns("/admin/**");
        registry.addInterceptor(new AuthorizationInterceptor(authService))
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/signup");
    }
}

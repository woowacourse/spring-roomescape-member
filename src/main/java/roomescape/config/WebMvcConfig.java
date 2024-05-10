package roomescape.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.interceptor.RoleInterceptor;
import roomescape.resolver.LoginMemberArgumentResolver;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    @Autowired
    public WebMvcConfig(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, loginMemberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor(authService, loginMemberService))
                .addPathPatterns("/admin/**");
    }
}

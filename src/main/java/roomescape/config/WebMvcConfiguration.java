package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.AuthenticatedUserArgumentResolver;
import roomescape.controller.CheckAdminInterceptor;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver;
    private final AuthService authService;
    private final MemberService memberService;

    public WebMvcConfiguration(AuthenticatedUserArgumentResolver authenticatedUserArgumentResolver, AuthService authService, MemberService memberService) {
        this.authenticatedUserArgumentResolver = authenticatedUserArgumentResolver;
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(authService, memberService))
                .addPathPatterns("/admin/**");
    }
}

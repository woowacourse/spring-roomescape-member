package roomescape.member.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.member.AuthService;
import roomescape.member.JwtTokenProvider;
import roomescape.member.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AuthService authService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfiguration(AuthService authService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckExpiredTokenInterceptor(jwtTokenProvider, authService))
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new CheckRoleInterceptor(authService, memberService))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, memberService));
    }
}

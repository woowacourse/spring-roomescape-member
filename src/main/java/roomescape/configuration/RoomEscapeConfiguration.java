package roomescape.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminInterceptor;
import roomescape.auth.JwtProvider;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class RoomEscapeConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public RoomEscapeConfiguration(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AdminInterceptor(memberService, jwtProvider));
        interceptorRegistration.addPathPatterns("/admin/**");
    }
}

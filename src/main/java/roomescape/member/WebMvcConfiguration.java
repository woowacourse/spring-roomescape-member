package roomescape.member;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.member.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AuthService authService;
    private final MemberService memberService;

    public WebMvcConfiguration(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, memberService));
    }
}

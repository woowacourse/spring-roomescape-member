package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.helper.CookieExtractor;
import roomescape.controller.helper.LoginMemberArgumentResolver;
import roomescape.service.MemberService;
import roomescape.service.helper.JwtTokenProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final CookieExtractor cookieExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService; // TODO: 체이닝 줄이는 법 있나 찾아보기(LoginMemberArgumentResolver의 component화?)

    public WebMvcConfig(CookieExtractor cookieExtractor,
                        JwtTokenProvider jwtTokenProvider,
                        MemberService memberService) {
        this.cookieExtractor = cookieExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(cookieExtractor, jwtTokenProvider, memberService));
    }
}

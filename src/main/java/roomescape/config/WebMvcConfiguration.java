package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.AuthorizationExtractor;
import roomescape.controller.LoginMemberArgumentResolver;
import roomescape.service.JwtTokenProvider;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public WebMvcConfiguration(MemberService memberService, JwtTokenProvider jwtTokenProvider, AuthorizationExtractor extractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider, extractor));
    }
}

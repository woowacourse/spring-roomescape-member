package roomescape.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.login.provider.JwtTokenProvider;
import roomescape.login.resolver.LoginMemberArgumentResolver;
import roomescape.login.service.MemberService;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider));
    }
}
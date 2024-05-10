package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.api.LoginMemberArgumentResolver;
import roomescape.controller.api.MemberIdArgumentResolver;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

//    @Autowired
    private final MemberService memberService;
//    @Autowired
    private final AuthService authService;

    public WebMvcConfiguration(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, authService));
        resolvers.add(new MemberIdArgumentResolver(authService));
    }
}

package roomescape.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.api.LoginMemberArgumentResolver;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthService authService;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, authService));
    }
}

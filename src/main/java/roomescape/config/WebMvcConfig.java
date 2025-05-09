package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.resolver.LoginMemberArgumentResolver;
import roomescape.service.LoginMemberService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginMemberService loginMemberService;

    public WebMvcConfig(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(loginMemberService));
    }
}

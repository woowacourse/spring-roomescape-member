package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.CheckLoginInterceptor;
import roomescape.controller.LoginMemberRequestArgumentResolver;
import roomescape.controller.MemberResponseArgumentResolver;
import roomescape.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MemberResponseArgumentResolver(memberService));
        resolvers.add(new LoginMemberRequestArgumentResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor(memberService)).addPathPatterns("/admin/**");
    }
}

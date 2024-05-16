package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.CheckAdminInterceptor;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.service.member.MemberService;
import roomescape.auth.MemberTokenConverter;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberTokenConverter memberTokenConverter;
    private final MemberService memberService;

    public WebMvcConfiguration(MemberTokenConverter memberTokenConverter, MemberService memberService) {
        this.memberTokenConverter = memberTokenConverter;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberTokenConverter, memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberTokenConverter, memberService))
                .addPathPatterns("/admin/**");
    }
}

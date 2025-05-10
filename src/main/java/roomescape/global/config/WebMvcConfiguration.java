package roomescape.global.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.admin.presentation.CheckAdminInterceptor;
import roomescape.member.application.service.MemberService;
import roomescape.member.presentation.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;

    @Autowired
    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberService))
                .addPathPatterns("/admin/**");
    }
}

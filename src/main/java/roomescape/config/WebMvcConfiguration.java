package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.service.MemberService;
import roomescape.ui.LoginMemberArgumentResolver;
import roomescape.ui.AdminPermissionInterceptor;
import roomescape.util.JwtTokenHelper;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenHelper jwtTokenHelper;
    private final MemberService memberService;

    public WebMvcConfiguration(JwtTokenHelper jwtTokenHelper, MemberService memberService) {
        this.jwtTokenHelper = jwtTokenHelper;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtTokenHelper));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminPermissionInterceptor(memberService , jwtTokenHelper))
                .addPathPatterns("/admin/**");
    }
}

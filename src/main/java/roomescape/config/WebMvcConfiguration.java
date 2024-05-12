package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.auth.CheckRoleInterceptor;
import roomescape.controller.auth.LoginMemberArgumentResolver;
import roomescape.controller.reservation.ReservationFilterArgumentResolver;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;

    @Autowired
    public WebMvcConfiguration(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService));
        resolvers.add(new ReservationFilterArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckRoleInterceptor(memberService))
                .addPathPatterns("/admin/**");
    }
}

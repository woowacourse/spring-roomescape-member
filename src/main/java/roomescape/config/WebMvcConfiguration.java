package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.auth.AuthorizationExtractor;
import roomescape.controller.auth.CheckRoleInterceptor;
import roomescape.controller.auth.LoginMemberArgumentResolver;
import roomescape.controller.reservation.ReservationFilterArgumentResolver;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    @Autowired
    public WebMvcConfiguration(final MemberService memberService,
                               final AuthorizationExtractor authorizationExtractor) {
        this.memberService = memberService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, authorizationExtractor));
        resolvers.add(new ReservationFilterArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckRoleInterceptor(memberService, authorizationExtractor))
                .addPathPatterns("/admin/**");
    }
}

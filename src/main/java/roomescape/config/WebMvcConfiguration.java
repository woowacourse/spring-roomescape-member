package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.CheckAdminInterceptor;
import roomescape.auth.CookieProvider;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;


    public WebMvcConfiguration(final CookieProvider cookieProvider, final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.cookieProvider = cookieProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(cookieProvider, jwtTokenProvider))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(cookieProvider, jwtTokenProvider, memberService));
    }
}

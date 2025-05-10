package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.controller.CheckMemberRoleInterceptor;
import roomescape.controller.LoginMemberArgumentResolver;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationExtractor extractor;

    public WebMvcConfiguration(MemberService memberService, JwtTokenProvider jwtTokenProvider, AuthorizationExtractor extractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractor = extractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider, extractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckMemberRoleInterceptor(jwtTokenProvider, extractor, memberService))
                .addPathPatterns("/admin/**");
    }
}

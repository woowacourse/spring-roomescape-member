package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckRoleInterceptor(memberService, jwtTokenProvider))
                .addPathPatterns("/admin/**");
    }
}

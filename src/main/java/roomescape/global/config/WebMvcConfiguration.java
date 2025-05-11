package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.interceptor.AdminAuthorizationInterceptor;
import roomescape.global.resolver.LoginMemberIdArgumentResolver;
import roomescape.global.security.JwtProvider;
import roomescape.member.repository.MemberRepository;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public WebMvcConfiguration(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(jwtProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthorizationInterceptor(memberRepository, jwtProvider))
                .addPathPatterns("/admin/**");
    }
}

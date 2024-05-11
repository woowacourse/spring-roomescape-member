package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthenticationPrincipalArgumentResolver;
import roomescape.auth.CheckAdminAccessInterceptor;
import roomescape.auth.TokenProvider;
import roomescape.domain.member.MemberRepository;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public WebMvcConfig(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(tokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminAccessInterceptor(tokenProvider, memberRepository))
                .addPathPatterns("/admin/**");
    }
}

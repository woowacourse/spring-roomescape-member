package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthenticationInfoExtractor;
import roomescape.auth.AuthenticationPrincipalArgumentResolver;
import roomescape.auth.CheckAdminAccessInterceptor;
import roomescape.domain.member.MemberRepository;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationInfoExtractor authenticationInfoExtractor;
    private final MemberRepository memberRepository;

    public WebMvcConfig(AuthenticationInfoExtractor authenticationInfoExtractor, MemberRepository memberRepository) {
        this.authenticationInfoExtractor = authenticationInfoExtractor;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authenticationInfoExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminAccessInterceptor(authenticationInfoExtractor, memberRepository))
                .addPathPatterns("/admin/**");
    }
}

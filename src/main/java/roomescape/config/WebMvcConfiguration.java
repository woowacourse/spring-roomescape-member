package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.MemberService;
import roomescape.infrastructure.AuthenticationPrincipalArgumentResolver;
import roomescape.infrastructure.CheckAdminRoleInterceptor;
import roomescape.infrastructure.MemberAuthRequestExtractor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final MemberAuthRequestExtractor memberAuthRequestExtractor;

    public WebMvcConfiguration(MemberService memberService, MemberAuthRequestExtractor memberAuthRequestExtractor) {
        this.memberService = memberService;
        this.memberAuthRequestExtractor = memberAuthRequestExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(
                new AuthenticationPrincipalArgumentResolver(memberAuthRequestExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                        new CheckAdminRoleInterceptor(
                                memberAuthRequestExtractor,
                                memberService))
                .addPathPatterns("/admin/**");
    }
}

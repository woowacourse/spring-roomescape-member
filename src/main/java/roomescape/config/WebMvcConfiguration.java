package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.CheckAdminInterceptor;
import roomescape.controller.CheckLoginInterceptor;
import roomescape.controller.LoginMemberArgumentResolver;
import roomescape.controller.RequestTokenContext;
import roomescape.service.MemberService;
import roomescape.util.TokenProvider;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RequestTokenContext requestTokenContext;

    public WebMvcConfiguration(final MemberService memberService, final TokenProvider tokenProvider, final RequestTokenContext requestTokenContext) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.requestTokenContext = requestTokenContext;
    }


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberService, tokenProvider, requestTokenContext))
                .addPathPatterns("/admin/**");
        registry.addInterceptor(new CheckLoginInterceptor(memberService,tokenProvider,requestTokenContext))
                .addPathPatterns("/login/check")
                .addPathPatterns("/reservations/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(requestTokenContext));
    }
}

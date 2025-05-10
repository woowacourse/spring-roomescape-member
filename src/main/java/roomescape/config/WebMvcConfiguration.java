package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.business.service.member.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final List<String> EXCLUDE_PATHS = List.of(
            "/signup",
            "/login",
            "/logout",
            "/error",
            "/css/**",
            "/js/**"
    );

    private final MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        addAuthenticationInterceptor(registry);
        addCheckAdminInterceptor(registry);
    }

    private void addAuthenticationInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(memberService))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    private void addCheckAdminInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor())
                .addPathPatterns("/admin/**");
    }
}

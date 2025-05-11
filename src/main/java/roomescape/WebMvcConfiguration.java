package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.service.MemberService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new VisitorArgumentResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminOnlyInterceptor(memberService))
                .addPathPatterns("/admin/**");
    }
}

package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.MemberService;
import roomescape.ui.MemberConverterArgumentResolver;
import roomescape.ui.CheckAdminInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        MemberConverterArgumentResolver argumentResolver = new MemberConverterArgumentResolver(memberService);
        resolvers.add(argumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CheckAdminInterceptor checkLoginInterceptor = new CheckAdminInterceptor();
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/admin/**");
    }
}

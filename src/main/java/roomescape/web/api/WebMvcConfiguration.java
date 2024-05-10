package roomescape.web.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.web.api.resolver.AdminAuthValidateInterceptor;
import roomescape.web.api.resolver.MemberArgumentResolver;
import roomescape.web.api.resolver.MemberAuthValidateInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberArgumentResolver memberArgumentResolver;
    private final AdminAuthValidateInterceptor adminAuthValidateInterceptor;
    private final MemberAuthValidateInterceptor memberAuthValidateInterceptor;

    public WebMvcConfiguration(MemberArgumentResolver memberArgumentResolver,
                               AdminAuthValidateInterceptor adminAuthValidateInterceptor,
                               MemberAuthValidateInterceptor memberAuthValidateInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.adminAuthValidateInterceptor = adminAuthValidateInterceptor;
        this.memberAuthValidateInterceptor = memberAuthValidateInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthValidateInterceptor)
                .addPathPatterns("/admin/**");
        registry.addInterceptor(memberAuthValidateInterceptor)
                .addPathPatterns("/reservation");
    }
}

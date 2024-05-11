package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.intereceptor.AdminInterceptor;
import roomescape.controller.resolver.MemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final MemberArgumentResolver memberArgumentResolver;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(
            MemberArgumentResolver memberArgumentResolver,
            AdminInterceptor adminInterceptor
    ) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin")
                .addPathPatterns("/admin/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

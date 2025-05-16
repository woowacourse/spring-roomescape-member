package roomescape.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import roomescape.auth.AdminInterceptor;
import roomescape.auth.MemberArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/reservation").setViewName("reservation");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/signup").setViewName("signup");

        registry.addViewController("/admin/reservation").setViewName("admin/reservation");
        registry.addViewController("/admin/time").setViewName("admin/time");
        registry.addViewController("/admin/theme").setViewName("admin/theme");

        registry.addRedirectViewController("/admin", "/admin/reservation");
    }
}

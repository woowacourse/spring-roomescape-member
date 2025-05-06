package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.presentation.AdminAuthorizationInterceptor;
import roomescape.auth.presentation.AuthenticatedMemberArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticatedMemberArgumentResolver resolver;
    private final AdminAuthorizationInterceptor adminInterceptor;

    public WebMvcConfiguration(AuthenticatedMemberArgumentResolver resolver, AdminAuthorizationInterceptor adminInterceptor) {
        this.resolver = resolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 사용자 페이지
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/reservation").setViewName("reservation");

        // 관리자 페이지
        registry.addViewController("/admin").setViewName("admin/index");
        registry.addViewController("/admin/reservation").setViewName("admin/reservation-new");
        registry.addViewController("/admin/time").setViewName("admin/time");
        registry.addViewController("/admin/theme").setViewName("admin/theme");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }
}

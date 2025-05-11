package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.presentation.AdminAuthorizationInterceptor;
import roomescape.auth.presentation.AuthenticatedMemberArgumentResolver;
import roomescape.auth.presentation.BasicAuthorizationInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthenticatedMemberArgumentResolver resolver;
    private final BasicAuthorizationInterceptor basicAuthInterceptor;
    private final AdminAuthorizationInterceptor adminAuthInterceptor;

    public WebMvcConfiguration(AuthenticatedMemberArgumentResolver resolver,
                               BasicAuthorizationInterceptor basicAuthInterceptor,
                               AdminAuthorizationInterceptor adminAuthInterceptor) {
        this.resolver = resolver;
        this.basicAuthInterceptor = basicAuthInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
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


    private final static List<String> STATIC_API_LIST = List.of(
            "/css/**",
            "/js/**",
            "/image/**",
            "/favicon.ico",
            "/docs/**"
    );

    private final static List<String> PUBLIC_API_LIST = List.of(
            "/",
            "/themes/ranking"
    );

    private final static List<String> PUBLIC_AUTH_API_LIST = List.of(
            "/login"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(basicAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(STATIC_API_LIST)
                .excludePathPatterns(PUBLIC_API_LIST)
                .excludePathPatterns(PUBLIC_AUTH_API_LIST);
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**");
    }
}

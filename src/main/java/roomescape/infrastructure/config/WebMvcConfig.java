package roomescape.infrastructure.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infrastructure.intercepter.AuthPreHandlerInterceptor;
import roomescape.infrastructure.intercepter.AuthenticationPrincipalResolver;
import roomescape.infrastructure.intercepter.ResponseHeaderInterceptor;
import roomescape.infrastructure.jwt.JwtTokenProvider;

@Configuration
@Import(AuthenticationConfig.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalResolver(jwtTokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthPreHandlerInterceptor(jwtTokenProvider))
                .addPathPatterns("/admin/**")
                .order(1);
        registry.addInterceptor(new ResponseHeaderInterceptor())
                .addPathPatterns("/auth/**")
                .order(2);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/reservation").setViewName("/reservation");

        //admin
        registry.addViewController("/admin").setViewName("/admin/index");
        registry.addViewController("/admin/reservation").setViewName("/admin/reservation-new");
        registry.addViewController("/admin/time").setViewName("/admin/time");
        registry.addViewController("/admin/theme").setViewName("/admin/theme");
    }
}

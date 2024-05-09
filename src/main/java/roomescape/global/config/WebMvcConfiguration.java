package roomescape.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.interceptor.CheckAdminInterceptor;
import roomescape.auth.interceptor.CheckMemberInterceptor;
import roomescape.auth.resolver.LoginUserArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final CheckMemberInterceptor checkMemberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkMemberInterceptor).order(1)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/reservation/**")
                .addPathPatterns("/reservations/**")
                .addPathPatterns("/logout");

        registry.addInterceptor(checkAdminInterceptor).order(2)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}

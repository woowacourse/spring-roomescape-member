package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.ui.interceptor.AuthenticationExtractInterceptor;
import roomescape.ui.interceptor.CheckAdminAccessInterceptor;
import roomescape.ui.support.AuthenticationPrincipalArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;
    private final AuthenticationExtractInterceptor authenticationExtractInterceptor;
    private final CheckAdminAccessInterceptor checkAdminAccessInterceptor;

    public WebMvcConfig(AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver,
                        AuthenticationExtractInterceptor authenticationExtractInterceptor,
                        CheckAdminAccessInterceptor checkAdminAccessInterceptor) {
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
        this.authenticationExtractInterceptor = authenticationExtractInterceptor;
        this.checkAdminAccessInterceptor = checkAdminAccessInterceptor;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationExtractInterceptor)
                .excludePathPatterns("/", "/login", "/logout", "/themes/popular")
                .excludePathPatterns("/css/**", "/js/**", "/image/**");
        registry.addInterceptor(checkAdminAccessInterceptor).addPathPatterns("/admin/**");
    }
}

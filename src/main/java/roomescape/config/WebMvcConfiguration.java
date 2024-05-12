package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final CheckLoginInterceptor checkLoginInterceptor;
    private final MemberIdConverterArgumentResolver memberIdConverterArgumentResolver;

    public WebMvcConfiguration(
            CheckAdminInterceptor checkAdminInterceptor,
            CheckLoginInterceptor checkLoginInterceptor,
            MemberIdConverterArgumentResolver memberIdConverterArgumentResolver
    ) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.checkLoginInterceptor = checkLoginInterceptor;
        this.memberIdConverterArgumentResolver = memberIdConverterArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdConverterArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/reservation", "/admin/**", "/login/check");
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

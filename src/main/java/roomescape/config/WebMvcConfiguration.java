package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.ui.CheckAdminInterceptor;
import roomescape.ui.MemberIdConverterArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final MemberIdConverterArgumentResolver memberIdConverterArgumentResolver;

    public WebMvcConfiguration(CheckAdminInterceptor checkAdminInterceptor,
                               MemberIdConverterArgumentResolver memberIdConverterArgumentResolver) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.memberIdConverterArgumentResolver = memberIdConverterArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdConverterArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

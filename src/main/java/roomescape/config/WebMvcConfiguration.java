package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.ui.CheckAdminInterceptor;
import roomescape.ui.MemberConverterArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final MemberConverterArgumentResolver memberConverterArgumentResolver;

    public WebMvcConfiguration(CheckAdminInterceptor checkAdminInterceptor,
                               MemberConverterArgumentResolver memberConverterArgumentResolver) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.memberConverterArgumentResolver = memberConverterArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberConverterArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

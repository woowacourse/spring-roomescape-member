package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.interceptor.CheckAdminInterceptor;
import roomescape.resolver.MemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final CheckAdminInterceptor checkAdminInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;

    public WebMvcConfig(
            CheckAdminInterceptor checkAdminInterceptor,
            MemberArgumentResolver memberArgumentResolver) {
        this.checkAdminInterceptor = checkAdminInterceptor;
        this.memberArgumentResolver = memberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}

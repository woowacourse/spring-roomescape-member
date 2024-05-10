package roomescape.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {


    private final MemberArgumentResolver memberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(
            MemberArgumentResolver memberArgumentResolver,
            CheckAdminInterceptor checkAdminInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor).addPathPatterns("/admin/**");
    }
}

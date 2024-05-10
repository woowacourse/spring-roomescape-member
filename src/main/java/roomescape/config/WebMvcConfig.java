package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final CheckLoginInterceptor checkLoginInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;

    public WebMvcConfig(
            CheckLoginInterceptor checkLoginInterceptor,
            MemberArgumentResolver memberArgumentResolver) {
        this.checkLoginInterceptor = checkLoginInterceptor;
        this.memberArgumentResolver = memberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}

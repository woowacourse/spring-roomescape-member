package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final CheckPermissionInterceptor checkPermissionInterceptor;

    public WebMvcConfiguration(
            MemberArgumentResolver memberArgumentResolver,
            CheckPermissionInterceptor checkPermissionInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.checkPermissionInterceptor = checkPermissionInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkPermissionInterceptor)
                .addPathPatterns("/**");
    }
}

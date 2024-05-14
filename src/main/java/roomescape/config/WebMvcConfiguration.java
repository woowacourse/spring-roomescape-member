package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.config.Interceptor.CheckAdminInterceptor;
import roomescape.config.resolver.TokenValueMethodArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TokenValueMethodArgumentResolver tokenValueMethodArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebMvcConfiguration(TokenValueMethodArgumentResolver tokenValueMethodArgumentResolver,
                               CheckAdminInterceptor checkAdminInterceptor) {
        this.tokenValueMethodArgumentResolver = tokenValueMethodArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenValueMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
    }

}

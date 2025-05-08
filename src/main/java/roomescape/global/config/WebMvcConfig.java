package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.interceptor.LogInterceptor;
import roomescape.global.jwt.TokenProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenProvider tokenProvider;
    private final HandlerMethodArgumentResolver handlerMethodArgumentResolver;


    public WebMvcConfig(TokenProvider tokenProvider, HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        this.tokenProvider = tokenProvider;
        this.handlerMethodArgumentResolver = handlerMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(handlerMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/h2-console", "/js/**", "/image/**", "/css/**");

//        registry.addInterceptor(new AuthorizationInterceptor(new CookieAuthorizationExtractor(), tokenProvider))
//                .order(2)
//                .excludePathPatterns("/login/**", "/signUp/**");
    }

}

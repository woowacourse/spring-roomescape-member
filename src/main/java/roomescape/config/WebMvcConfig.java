package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.CheckAuthInterceptor;
import roomescape.controller.LoginMemberArgumentResolver;
import roomescape.util.JwtTokenProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver argumentResolver;
    private final JwtTokenProvider tokenProvider;

    public WebMvcConfig(LoginMemberArgumentResolver argumentResolver, JwtTokenProvider tokenProvider) {
        this.argumentResolver = argumentResolver;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(argumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAuthInterceptor(tokenProvider)).addPathPatterns("/admin/**");
    }
}

package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.service.TokenLoginService;
import roomescape.presentation.argumentresolver.MemberArgumentResolver;
import roomescape.presentation.interceptor.CheckAdminInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenLoginService tokenLoginService;

    public WebConfig(final TokenLoginService tokenLoginService) {
        this.tokenLoginService = tokenLoginService;
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MemberArgumentResolver(tokenLoginService));
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(tokenLoginService))
                .addPathPatterns("/admin/**");
    }
}

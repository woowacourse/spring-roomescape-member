package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.CheckAdminInterceptor;
import roomescape.controller.LoginArgumentResolver;
import roomescape.service.LoginService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginService loginService;

    public WebMvcConfiguration(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor())
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(loginService));
    }
}

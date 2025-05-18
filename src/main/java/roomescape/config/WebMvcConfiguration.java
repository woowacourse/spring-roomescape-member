package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.service.LoginService;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginService loginService;

    public WebMvcConfiguration(LoginService loginService) {
        this.loginService =  loginService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(loginService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthInterceptor(loginService))
            .addPathPatterns("/admin/**");
    }
}

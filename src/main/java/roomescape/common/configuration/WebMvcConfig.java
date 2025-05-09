package roomescape.common.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.login.presentation.AuthHandlerInterceptor;
import roomescape.auth.login.presentation.argumentresolver.LoginAdminArgumentResolver;
import roomescape.auth.login.presentation.argumentresolver.LoginMemberArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginAdminArgumentResolver());
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthHandlerInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}

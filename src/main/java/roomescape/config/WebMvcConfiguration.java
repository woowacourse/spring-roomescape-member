package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginContext loginContext;

    public WebMvcConfiguration(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(loginContext));
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        addCheckAdminInterceptor(registry);
    }

    private void addCheckAdminInterceptor(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(loginContext))
                .addPathPatterns("/admin/**");
    }
}

package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.ui.CheckAdminInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CheckAdminInterceptor checkLoginInterceptor = new CheckAdminInterceptor();
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/admin/**");
    }
}

package roomescape.reservation.resolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.service.AutoService;

@Configuration
public class AdminInterceptorConfig implements WebMvcConfigurer {

    private final AutoService autoService;

    public AdminInterceptorConfig(AutoService autoService) {
        this.autoService = autoService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthorizationInterceptor(autoService))
                .addPathPatterns("/admin/**");
    }
}

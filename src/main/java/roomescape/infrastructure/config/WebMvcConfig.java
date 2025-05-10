package roomescape.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import(AuthenticationConfig.class)
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/reservation").setViewName("/reservation");

        //admin
        registry.addViewController("/admin").setViewName("/admin/index");
        registry.addViewController("/admin/reservation").setViewName("/admin/reservation-with-member");
        registry.addViewController("/admin/time").setViewName("/admin/time");
        registry.addViewController("/admin/theme").setViewName("/admin/theme");
    }
}

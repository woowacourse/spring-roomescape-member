package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("/admin/index");
        registry.addViewController("/admin/reservation").setViewName("/admin/reservation-new");
        registry.addViewController("/admin/time").setViewName("/admin/time");
        registry.addViewController("/admin/theme").setViewName("/admin/theme");
    }
}

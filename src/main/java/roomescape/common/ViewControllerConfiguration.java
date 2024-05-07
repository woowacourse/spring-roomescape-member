package roomescape.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewControllerConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);

        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/reservation").setViewName("reservation");

        registry.addViewController("/admin").setViewName("/admin/index");
        registry.addViewController("/admin/reservation").setViewName("/admin/reservation-new");
        registry.addViewController("/admin/time").setViewName("/admin/time");
        registry.addViewController("/admin/theme").setViewName("/admin/theme");

    }
}

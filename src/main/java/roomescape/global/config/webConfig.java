package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.converter.StringToMemberNameConverter;
import roomescape.global.converter.StringToReservationDateConverter;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToMemberNameConverter());
        registry.addConverter(new StringToReservationDateConverter());
    }
}

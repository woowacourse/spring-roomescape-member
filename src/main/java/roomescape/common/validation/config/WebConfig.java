package roomescape.common.validation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.validation.validator.SpringCustomValidator;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        return new SpringCustomValidator();
    }

}

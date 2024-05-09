package roomescape.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.infrastructure.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new roomescape.infrastructure.PasswordEncoder();
    }
}

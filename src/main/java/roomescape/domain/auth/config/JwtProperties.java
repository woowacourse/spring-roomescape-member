package roomescape.domain.auth.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@Setter
@Validated
public class JwtProperties {

    @NotBlank
    private String cookieKey;
    @NotNull
    private int expireLength;
    @NotBlank
    private String secretKey;
}

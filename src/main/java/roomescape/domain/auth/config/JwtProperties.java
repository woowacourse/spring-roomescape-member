package roomescape.domain.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@Setter
public class JwtProperties {
    private String cookieKey;
    private int expireLength;
    private String secretKey;
}

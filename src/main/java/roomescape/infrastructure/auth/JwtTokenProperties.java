package roomescape.infrastructure.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
public class JwtTokenProperties {
    private String secretKey;
    private long validityInMilliseconds;

    public String getSecretKey() {
        return secretKey;
    }

    public long getValidityInMilliseconds() {
        return validityInMilliseconds;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setValidityInMilliseconds(long validityInMilliseconds) {
        this.validityInMilliseconds = validityInMilliseconds;
    }
}

package roomescape.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt.token")
public class JwtProperties {

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtProperties(String secretKey, long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public long getValidityInMilliseconds() {
        return validityInMilliseconds;
    }
}

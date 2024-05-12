package roomescape.infrastructure.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
public class JwtTokenProperties {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60;
    private String secretKey;
    private long expireMinute;

    public String getSecretKey() {
        return secretKey;
    }

    public long getExpireMinute() {
        return expireMinute;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireMinute(long expireMinute) {
        this.expireMinute = expireMinute * MINUTE * SECOND;
    }
}

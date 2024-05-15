package roomescape.infrastructure.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
record JwtProperties(String secretKey, Long tokenExpirationSecond) {
}

package roomescape.web.api.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
record JwtProperties(String secretKey) {
}

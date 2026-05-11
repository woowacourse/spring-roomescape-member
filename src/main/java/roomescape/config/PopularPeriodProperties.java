package roomescape.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "roomescape.popular-theme")
public record PopularPeriodProperties(int startDaysAgo, int endDaysAgo) {
}

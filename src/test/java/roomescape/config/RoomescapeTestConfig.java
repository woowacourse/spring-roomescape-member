package roomescape.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import roomescape.fixture.CookieFixture;
import roomescape.util.JwtProvider;
import roomescape.util.TokenProvider;

@TestConfiguration
public class RoomescapeTestConfig {

    @Bean
    public TokenProvider tokenProvider() {
        String key = "woowacoursewoowacoursewoowacourse";
        long oneHourInMilliseconds = 60 * 60 * 1000;
        return new JwtProvider(key, oneHourInMilliseconds);
    }

    @Bean
    public CookieFixture cookieFixture() {
        return new CookieFixture(tokenProvider());
    }
}

package roomescape.util.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.TokenExtractor;

@TestConfiguration
public class WebMvcTestConfig {

    @Bean
    public TokenExtractor tokenExtractor() {
        return Mockito.mock(TokenExtractor.class);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return Mockito.mock(JwtTokenProvider.class);
    }

}

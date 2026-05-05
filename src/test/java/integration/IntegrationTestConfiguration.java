package integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.repository.ThemeRepository;
import roomescape.service.fake.FakeThemeRepository;

@TestConfiguration
public class IntegrationTestConfiguration {

    @Bean
    public ThemeRepository themeRepository() {
        return new FakeThemeRepository();
    }
}

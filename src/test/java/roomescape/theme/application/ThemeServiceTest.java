package roomescape.theme.application;


import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.AlreadyExistException;
import roomescape.theme.applcation.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infrastructure.JdbcThemeRepository;
import roomescape.theme.ui.dto.CreateThemeRequest;
import roomescape.theme.ui.dto.ThemeResponse;

@JdbcTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 테마를_저장한다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final CreateThemeRequest request = new CreateThemeRequest(name, description, thumbnail);

        // when & then
        Assertions.assertThatCode(() -> {
            themeService.create(request);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마를_삭제한다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final Theme theme = new Theme(name, description, thumbnail);
        final Long id = themeRepository.save(theme);

        // when & then
        Assertions.assertThatCode(() -> {
            themeService.delete(id);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마_전체를_조회한다() {
        // given
        final String name1 = "우가우가";
        final String description1 = "우가우가 설명";
        final String thumbnail1 = "따봉우가.jpg";
        final Theme theme1 = new Theme(name1, description1, thumbnail1);
        themeRepository.save(theme1);

        final String name2 = "우가우가2";
        final String description2 = "우가우가2 설명";
        final String thumbnail2 = "따봉우가2.jpg";
        final Theme theme2 = new Theme(name2, description2, thumbnail2);
        themeRepository.save(theme2);

        // when
        final List<ThemeResponse> themes = themeService.findAll();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(themes).hasSize(2);
            softly.assertThat(themes.get(0).name()).isEqualTo(name1);
            softly.assertThat(themes.get(0).description()).isEqualTo(description1);
            softly.assertThat(themes.get(0).thumbnail()).isEqualTo(thumbnail1);
            softly.assertThat(themes.get(1).name()).isEqualTo(name2);
            softly.assertThat(themes.get(1).description()).isEqualTo(description2);
            softly.assertThat(themes.get(1).thumbnail()).isEqualTo(thumbnail2);
        });
    }

    @Test
    void 테마_이름은_중복_될_수_없다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final Theme theme = new Theme(name, description, thumbnail);
        themeRepository.save(theme);

        final CreateThemeRequest request = new CreateThemeRequest(name, description, thumbnail);

        // when & then
        Assertions.assertThatThrownBy(() -> themeService.create(request))
                .isInstanceOf(AlreadyExistException.class);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ThemeRepository themeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcThemeRepository(jdbcTemplate);
        }

        @Bean
        public ThemeService themeService(
                final ThemeRepository themeRepository
        ) {
            return new ThemeService(themeRepository);
        }
    }
}

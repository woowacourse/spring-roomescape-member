package roomescape.theme.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.ThemeRepository;
import roomescape.theme.domain.Theme;

@JdbcTest
public class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @DisplayName("db의 정상 저장을 테스트 합니다.")
    @Test
    void save_successfully() {
        Theme theme = Theme.create("theme name", "theme description", "theme img url");

        Theme savedTheme = themeRepository.save(theme);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(savedTheme).isNotNull();
            assertSoftly.assertThat(savedTheme.getId()).isEqualTo(1L);
            assertSoftly.assertThat(savedTheme.getName()).isEqualTo("theme name");
            assertSoftly.assertThat(savedTheme.getDescription()).isEqualTo("theme description");
            assertSoftly.assertThat(savedTheme.getThumbnailImgUrl()).isEqualTo("theme img url");
        });
    }
}

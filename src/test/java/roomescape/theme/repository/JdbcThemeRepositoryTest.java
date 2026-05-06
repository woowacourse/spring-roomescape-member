package roomescape.theme.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
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
        Theme theme = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(savedTheme).isNotNull();
            assertSoftly.assertThat(savedTheme.getId()).isPositive();
            assertSoftly.assertThat(savedTheme.getName()).isEqualTo("theme name");
            assertSoftly.assertThat(savedTheme.getDescription()).isEqualTo("theme description");
            assertSoftly.assertThat(savedTheme.getThumbnailImgUrl()).isEqualTo("theme img url");
        });
    }

    @DisplayName("db에 특정 테마가 존재하지 않는 것을 테스트 합니다.")
    @Test
    void check_none_exists_successfully() {
        Theme theme = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();

        Boolean alreadyExists = themeRepository.existsByNameAndDescription(theme);

        assertThat(alreadyExists).isFalse();
    }

    @DisplayName("db에 특정 테마가 존재하는 것을 테스트 합니다.")
    @Test
    void check_exists_successfully() {
        Theme theme1 = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();
        themeRepository.save(theme1);

        Theme theme2 = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();
        Boolean alreadyExists = themeRepository.existsByNameAndDescription(theme2);
        assertThat(alreadyExists).isTrue();
    }

    @DisplayName("db에서 테마 삭제를 테스트 합니다.")
    @Test
    void delete_theme_successfully() {
        Theme theme1 = Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build();
        Theme savedTheme = themeRepository.save(theme1);

        assertThat(themeRepository.delete(savedTheme.getId())).isEqualTo(1);
    }

    @DisplayName("db에서 테마를 전체 조회합니다.")
    @Test
    void find_all_themes() {
        assertThat(themeRepository.findAll().size()).isEqualTo(10);
    }

    @DisplayName("db에서 최근 7일간 인기있던 테마 상위 10개를 조회를 테스트합니다.")
    @Test
    void find_popular_10_themes_during_recent_7days() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<PopularTheme> popularThemes = themeRepository.findTop10PopularThemesBetween(yesterday.minusWeeks(1), yesterday);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(popularThemes.getFirst().id()).isEqualTo(2L);
            assertSoftly.assertThat(popularThemes.getFirst().name()).isEqualTo("SF 우주 탐험");
            assertSoftly.assertThat(popularThemes.get(4).id()).isEqualTo(6L);
            assertSoftly.assertThat(popularThemes.get(4).name()).isEqualTo("비밀 연구소");
        });
    }
}

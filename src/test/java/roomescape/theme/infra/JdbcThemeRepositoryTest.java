package roomescape.theme.infra;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.support.TestDataHelper;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

@JdbcTest
public class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ThemeRepository themeRepository;
    ReservationTimeRepository timeRepository;
    TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        testHelper = new TestDataHelper(jdbcTemplate);
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
        Theme savedTheme = themeRepository.save(Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build());

        assertThat(themeRepository.delete(savedTheme.getId())).isEqualTo(1);
    }

    @DisplayName("db에서 테마를 전체 조회합니다.")
    @Test
    void find_all_themes() {
        themeRepository.save(Theme.builder()
                .name("theme name 1")
                .description("theme description 1")
                .thumbnailImgUrl("theme img url 1")
                .build()
        );
        themeRepository.save(Theme.builder()
                .name("theme name 2")
                .description("theme description 2")
                .thumbnailImgUrl("theme img url 2")
                .build()
        );
        themeRepository.save(Theme.builder()
                .name("theme name 3")
                .description("theme description 3")
                .thumbnailImgUrl("theme img url 3")
                .build()
        );

        assertThat(themeRepository.findAll().size()).isEqualTo(3);
    }
}

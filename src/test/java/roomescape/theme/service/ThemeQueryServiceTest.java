package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ThemeFixture;
import roomescape.support.TestDataHelper;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.application.dto.PopularThemeResult;
import roomescape.theme.application.service.ThemeCommandService;
import roomescape.theme.application.service.ThemeQueryService;
import roomescape.theme.infra.JdbcPopularThemeDao;
import roomescape.theme.infra.JdbcThemeRepository;

@JdbcTest
@Import({ThemeCommandService.class, ThemeQueryService.class, JdbcThemeRepository.class, JdbcPopularThemeDao.class})
public class ThemeQueryServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeCommandService themeCommandService;

    @Autowired
    private ThemeQueryService themeQueryService;

    private TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("테마 조회를 테스트합니다.")
    @Test
    void find_theme() {
        ThemeResult savedTheme = themeCommandService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(themeQueryService.findById(savedTheme.id()))
                .isEqualTo(ThemeFixture.horrorThemeQueryResult(savedTheme.id()));
    }

    @DisplayName("존재하지 않는 테마 조회 시 예외 발생을 테스트합니다.")
    @Test
    void theme_not_exists() {
        assertThatThrownBy(() -> themeQueryService.findById(100L))
                .isInstanceOf(ThemeException.class)
                .hasMessage("존재하지 않는 테마 입니다.");
    }

    @DisplayName("테마의 전체 조회를 테스트합니다.")
    @Test
    void find_all_themes() {
        ThemeResult firstTheme = themeCommandService.save(ThemeFixture.themeCreateCommand(1));
        ThemeResult secondTheme = themeCommandService.save(ThemeFixture.themeCreateCommand(2));
        ThemeResult thirdTheme = themeCommandService.save(ThemeFixture.themeCreateCommand(3));

        List<ThemeResult> themes = themeQueryService.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(themes.size()).isEqualTo(3);
            softly.assertThat(themes).containsExactly(
                    ThemeFixture.themeQueryResult(1, firstTheme.id()),
                    ThemeFixture.themeQueryResult(2, secondTheme.id()),
                    ThemeFixture.themeQueryResult(3, thirdTheme.id())
            );
        });
    }

    @DisplayName("인기 테마 조회를 테스트합니다.")
    @Test
    void find_popular_themes() {
        LocalDate today = LocalDate.of(2026, 5, 6);
        Long nineTimeId = testHelper.insertReservationTime(LocalTime.of(9, 0));
        Long tenTimeId = testHelper.insertReservationTime(LocalTime.of(10, 0));

        ThemeResult firstTheme = themeCommandService.save(ThemeFixture.themeCreateCommand(1));
        ThemeResult secondTheme = themeCommandService.save(ThemeFixture.themeCreateCommand(2));

        testHelper.insertReservation("테마1 예약자1", today.minusDays(1), firstTheme.id(), nineTimeId);
        testHelper.insertReservation("테마1 예약자2", today.minusDays(1), firstTheme.id(), tenTimeId);
        testHelper.insertReservation("테마2 예약자1", today.minusDays(2), secondTheme.id(), nineTimeId);
        testHelper.insertReservation("기간 밖 예약자", today.minusDays(8), secondTheme.id(), tenTimeId);

        List<PopularThemeResult> responses = themeQueryService.findPopularThemes(today);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(responses).containsExactly(
                    new PopularThemeResult(firstTheme.id(), "테마 1", "테마 설명 1", "http://img.url", 2),
                    new PopularThemeResult(secondTheme.id(), "테마 2", "테마 설명 2", "http://img.url", 1)
            );
        });
    }
}

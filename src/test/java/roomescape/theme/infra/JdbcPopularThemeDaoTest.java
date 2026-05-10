package roomescape.theme.infra;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.support.TestDataHelper;
import roomescape.theme.application.query.PopularTheme;
import roomescape.theme.application.query.PopularThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

@JdbcTest
public class JdbcPopularThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    PopularThemeDao themeDao;
    ThemeRepository themeRepository;
    ReservationTimeRepository timeRepository;
    TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        themeDao = new JdbcPopularThemeDao(jdbcTemplate);
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("db에서 최근 7일간 인기있던 테마 상위 10개를 조회를 테스트합니다.")
    @Test
    void find_popular_10_themes_during_recent_7days() {
        ReservationTime time1 = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());
        ReservationTime time2 = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(10, 0))
                .build());

        LocalDate today = LocalDate.of(2026, 5, 10);
        LocalDate yesterday = today.minusDays(1);
        Theme theme1 = themeRepository.save(Theme.builder().name("theme name 1").description("theme description 1").thumbnailImgUrl("theme img url 1").build());
        Theme theme2 = themeRepository.save(Theme.builder().name("theme name 2").description("theme description 2").thumbnailImgUrl("theme img url 2").build());

        testHelper.insertReservation("스타크", yesterday, theme1.getId(), time1.getId());
        testHelper.insertReservation("카야", yesterday, theme2.getId(), time2.getId());
        testHelper.insertReservation("스타크", yesterday, theme1.getId(), time2.getId());

        List<PopularTheme> popularThemes = themeDao.findTop10PopularThemesBetween(today.minusWeeks(1), today.minusDays(1));

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(popularThemes.getFirst().id()).isEqualTo(theme1.getId());
            assertSoftly.assertThat(popularThemes.getFirst().reservedCount()).isEqualTo(2);
            assertSoftly.assertThat(popularThemes.get(1).id()).isEqualTo(theme2.getId());
            assertSoftly.assertThat(popularThemes.get(1).reservedCount()).isEqualTo(1);
        });
    }
}

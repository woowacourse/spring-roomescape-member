package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import(ThemeDao.class)
class ThemeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("특정 테마와 날짜에 대해 예약되지 않은 시간 목록만 조회한다.")
    void 예약되지_않은_시간_조회_테스트() {
        // given
        Long themeId = 1L;
        String date = LocalDate.now().minusDays(7).toString();

        // when
        List<ReservationTime> availableTimes = themeDao.findAvailableTime(themeId, date);

        // then
        assertThat(availableTimes).hasSize(4);
        assertThat(availableTimes.get(0).getStartAt()).isEqualTo("15:00");
    }

    @Test
    @DisplayName("최근 7일간 예약이 많은 순서대로 테마를 조회한다.")
    void 최근_7일간_예약이_많은_테마_조회_테스트() {
        // given
        String today = "2026-05-08";
        int expected = 3;

        // when
        List<Theme> topThemes = themeDao.findTopThemes(3L);

        // then
        assertThat(topThemes).hasSize(expected);
        assertThat(topThemes.get(0).getName()).isEqualTo("우테코 공포물");
        assertThat(topThemes.get(1).getName()).isEqualTo("미래 도시");
        assertThat(topThemes.get(2).getName()).isEqualTo("고대 이집트");
    }
}

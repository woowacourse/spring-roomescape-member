package roomescape.service.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.exception.IllegalUserRequestException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ThemeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Sql("/truncate-data.sql")
class ThemeServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ThemeService themeService;

    @Autowired
    public ThemeServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        themeService = new ThemeService(
                new ThemeRepository(jdbcTemplate),
                new ReservationRepository(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("예약 중이 아닌 테마를 삭제할 시 성공한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void deleteNotReservedTime_Success() {
        assertThatCode(() -> themeService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약 중인 테마를 삭제할 시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql", "/reservation-data.sql"})
    void deleteReservedTime_Failure() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalUserRequestException.class)
                .hasMessage("이미 예약중인 테마는 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("최근 7일간 가장 예약이 많이된 테마 10개를 조회한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-rank-test-data.sql"})
    void test() {
        List<Long> themeIds = themeService.findTop10Recent7Days().stream()
                .map(Theme::getId)
                .toList();
        assertThat(themeIds)
                .isEqualTo(List.of(
                        5L, 2L, 3L, 7L, 10L, 1L, 4L, 6L, 8L, 9L
                ));
    }
}

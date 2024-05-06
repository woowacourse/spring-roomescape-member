package roomescape.service.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ThemeService;

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
    @Sql("/theme-time-data.sql")
    void deleteNotReservedTime_Success() {
        assertThatCode(() -> themeService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약 중인 테마를 삭제할 시 예외가 발생한다.")
    @Sql(scripts = {"/theme-time-data.sql", "/reservation-data.sql"})
    void deleteReservedTime_Failure() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약중인 테마는 삭제할 수 없습니다.");
    }
}

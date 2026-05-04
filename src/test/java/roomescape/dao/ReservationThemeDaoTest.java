package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

public class ReservationThemeDaoTest extends BaseDaoTest {
    private ReservationThemeDao reservationThemeDao;

    @Override
    protected void initTable() {
        createReservationThemeTable();
        insertReservationTheme("테마1", "테마 설명", "image url");

        this.reservationThemeDao = new ReservationThemeDao(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationThemeTable();
    }

    @Test
    @DisplayName("특정 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationThemeTest() {
        Optional<ThemeDaoData> reservationTheme = reservationThemeDao.getTheme(1L);

        assertThat(reservationTheme.isPresent()).isTrue();
        assertThat(reservationTheme.get()).isEqualTo(new ThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 테마 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationThemTest() {
        Optional<ThemeDaoData> reservationTheme = reservationThemeDao.getTheme(3L);

        assertThat(reservationTheme.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationTimesTest() {
        List<ThemeDaoData> reservationTimes = reservationThemeDao.getAllTheme();

        assertThat(reservationTimes).containsExactly(new ThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationThemeDao.deleteTheme(1);
        List<ThemeDaoData> reservationTimes = reservationThemeDao.getAllTheme();

        assertThat(reservationTimes).isNotIn(new ThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        ThemeDaoData reservationTheme = reservationThemeDao.addTheme(new ThemeCommand( "테마2", "테마 설명", "image url"));
        List<ThemeDaoData> reservations = reservationThemeDao.getAllTheme();

        ThemeDaoData expectedReservationTheme = new ThemeDaoData(2, "테마2", "테마 설명", "image url");

        assertThat(reservationTheme).isEqualTo(expectedReservationTheme);
        assertThat(reservations).contains(expectedReservationTheme);
    }
}

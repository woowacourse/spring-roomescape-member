package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.domain.ReservationTheme.ReservationThemeDaoData;

public class ReservationReservationThemeDaoTest extends BaseDaoTest {
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
        Optional<ReservationThemeDaoData> reservationTheme = reservationThemeDao.getTheme(1L);

        assertThat(reservationTheme.isPresent()).isTrue();
        assertThat(reservationTheme.get()).isEqualTo(new ReservationThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 테마 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationThemTest() {
        Optional<ReservationThemeDaoData> reservationTheme = reservationThemeDao.getTheme(3L);

        assertThat(reservationTheme.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationTimesTest() {
        List<ReservationThemeDaoData> reservationTimes = reservationThemeDao.getAllTheme();

        assertThat(reservationTimes).containsExactly(new ReservationThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationThemeDao.deleteTheme(1);
        List<ReservationThemeDaoData> reservationTimes = reservationThemeDao.getAllTheme();

        assertThat(reservationTimes).isNotIn(new ReservationThemeDaoData(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        ReservationThemeDaoData reservationTheme = reservationThemeDao.addTheme(new ReservationThemeCommand( "테마2", "테마 설명", "image url"));
        List<ReservationThemeDaoData> reservations = reservationThemeDao.getAllTheme();

        ReservationThemeDaoData expectedReservationTheme = new ReservationThemeDaoData(2, "테마2", "테마 설명", "image url");

        assertThat(reservationTheme).isEqualTo(expectedReservationTheme);
        assertThat(reservations).contains(expectedReservationTheme);
    }
}

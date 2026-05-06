package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.repository.ReservationTheme.JdbcReservationThemeRepository;
import roomescape.repository.ReservationTheme.ReservationThemeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationThemeRepositoryTest extends BaseRepositoryTest {
    private ReservationThemeRepository reservationThemeRepository;

    @Override
    protected void initTable() {
        createReservationThemeTable();
        insertReservationTheme("테마1", "테마 설명", "image url");

        this.reservationThemeRepository = new JdbcReservationThemeRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationThemeTable();
    }

    @Test
    @DisplayName("특정 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationThemeTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeRepository.getTheme(1L);

        assertThat(reservationTheme.isPresent()).isTrue();
        assertThat(reservationTheme.get()).isEqualTo(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 테마 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationThemTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeRepository.getTheme(3L);

        assertThat(reservationTheme.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationTimesTest() {
        List<ReservationTheme> reservationTimes = reservationThemeRepository.getAllTheme();

        assertThat(reservationTimes).containsExactly(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationThemeRepository.deleteTheme(1);
        List<ReservationTheme> reservationTimes = reservationThemeRepository.getAllTheme();

        assertThat(reservationTimes).isNotIn(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        ReservationTheme reservationTheme = reservationThemeRepository.addTheme(new ReservationThemeCommand( "테마2", "테마 설명", "image url"));
        List<ReservationTheme> reservations = reservationThemeRepository.getAllTheme();

        ReservationTheme expectedReservationTheme = new ReservationTheme(2, "테마2", "테마 설명", "image url");

        assertThat(reservationTheme).isEqualTo(expectedReservationTheme);
        assertThat(reservations).contains(expectedReservationTheme);
    }
}

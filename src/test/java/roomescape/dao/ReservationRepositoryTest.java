package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTheme.ReservationTheme;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;

public class ReservationRepositoryTest extends BaseRepositoryTest {
    private ReservationRepository reservationRepository;

    private final Reservation INIT_RESERVATION = new Reservation(
            1,
            "브라운",
            "2023-08-05",
            new ReservationTime(1, "10:00"),
            new ReservationTheme(1, "테마1", "테마 설명", "image url")
    );

    @Override
    protected void initTable() {
        createReservationTimeTable();
        createReservationThemeTable();
        createReservationTable();

        insertReservationTime("10:00");
        insertReservationTheme("테마1", "테마 설명", "image url");
        insertReservation("브라운", "2023-08-05", 1, 1);
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationTable();
        deleteReservationTimeTable();
    }

    @Test
    @DisplayName("전체 예약 테스트 정상적으로 가져오는 지 테스트")
    void getReservationTest() {
        List<Reservation> reservations = reservationRepository.getAllReservation();

        assertThat(reservations).containsExactly(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationRepository.deleteReservation(1);
        List<Reservation> reservations = reservationRepository.getAllReservation();

        assertThat(reservations).isNotIn(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        reservationRepository.addReservation(new ReservationCommand("테스트", "2023-08-15", 1, 1), new ReservationTime(1, "15:14"), new ReservationTheme(1, "theme", "description", "imageUrl"));

        List<Reservation> reservations = reservationRepository.getAllReservation();

        Reservation expectedReservation = new Reservation(2, "테스트", "2023-08-15", new ReservationTime(1, "10:00"), new ReservationTheme(1, "테마1", "테마 설명", "image url"));

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations).contains(expectedReservation);
    }
}

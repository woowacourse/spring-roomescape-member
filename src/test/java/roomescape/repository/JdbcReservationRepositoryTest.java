package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.repository.rowmapper.ReservationRowMapper;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private DataSource dataSource;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    private final ReservationTime time1 = new ReservationTime(1L, "00:00");
    private final ReservationTime time2 = new ReservationTime(2L, "12:00");

    private final Reservation reservation1 = new Reservation(null, "안돌", "2024-09-08", 1L, "00:00");
    private final Reservation reservation2 = new Reservation(null, "재즈", "2024-11-30", 2L, "12:00");

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(dataSource, new ReservationRowMapper());
        reservationTimeRepository = new JdbcReservationTimeRepository(dataSource, new ReservationTimeRowMapper());
        initializeTimesData();
    }

    private void initializeTimesData() {
        reservationTimeRepository.insertReservationTime(time1);
        reservationTimeRepository.insertReservationTime(time2);
    }

    @Test
    @DisplayName("저장된 모든 예약 정보를 가져온다")
    void find_all_reservations() {
        reservationRepository.insertReservation(reservation1);
        reservationRepository.insertReservation(reservation2);

        List<Reservation> allReservations = reservationRepository.findAllReservations();

        assertThat(allReservations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save_reservation() {
        Reservation reservation = reservationRepository.insertReservation(reservation2);

        assertAll(
                () -> assertThat(reservation.getName()).isEqualTo("재즈"),
                () -> assertThat(reservation.getDate()).isEqualTo("2024-11-30"),
                () -> assertThat(reservation.getTimeId()).isEqualTo(2),
                () -> assertThat(reservation.getTimeStartAt()).isEqualTo("12:00")
        );
    }

    @Test
    @DisplayName("예약을 id로 삭제한다.")
    void delete_reservation_by_id() {
        reservationRepository.insertReservation(reservation1);
        int beforeSize = reservationRepository.findAllReservations().size();

        reservationRepository.deleteReservationById(1L);
        int afterSize = reservationRepository.findAllReservations().size();

        assertAll(
                () -> assertThat(beforeSize).isEqualTo(1),
                () -> assertThat(afterSize).isEqualTo(0)
        );
    }
}

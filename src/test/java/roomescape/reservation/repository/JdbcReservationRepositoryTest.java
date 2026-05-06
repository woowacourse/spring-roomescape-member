package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.Reservation;

@JdbcTest
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Test
    void 사용자_이름으로_예약_목록_조회() {
        String userName = "동키";

        List<Reservation> reservations = reservationRepository.findByUserName(userName);

        assertThat(reservations.size()).isEqualTo(5);
    }

    @Test
    void 테마와_날짜로_예약_목록_조회() {
        Long themeId = 2L;
        LocalDate date = LocalDate.of(2026,05,10);

        List<Reservation> reservations = reservationRepository.findByThemeAndDate(themeId, date);

        assertThat(reservations.getFirst().getId()).isEqualTo(4);
    }
}

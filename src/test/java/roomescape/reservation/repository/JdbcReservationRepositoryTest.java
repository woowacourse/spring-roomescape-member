package roomescape.reservation.repository;

import java.time.LocalDate;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.infra.JdbcReservationRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @DisplayName("사용자의 방탈출 예약 시간 추가를 테스트합니다.")
    @Test
    void save_user_reservation() {
        Reservation reservation = Reservation.builder()
                .name("name")
                .date(LocalDate.of(2026, 5, 4))
                .themeId(1L)
                .timeId(1L)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(savedReservation.getName()).isEqualTo("name");
            assertSoftly.assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertSoftly.assertThat(savedReservation.getThemeId()).isEqualTo(1L);
            assertSoftly.assertThat(savedReservation.getTimeId()).isEqualTo(1L);
        });
    }
}

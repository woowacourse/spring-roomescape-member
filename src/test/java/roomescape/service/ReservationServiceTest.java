package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.repository.FakeReservationRepository;
import roomescape.repository.ReservationRepository;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    ReservationService reservationService;

    @Nested
    @DisplayName("예약 조회")
    class FindReservation {

        @DisplayName("모든 Reservation을 조회할 수 있다")
        @Test
        void findAllReservationResponsesTest() {
            LocalTime startTime = LocalTime.of(10, 0);
            ReservationTime reservationTime = new ReservationTime(1L, startTime);
            Theme theme = new Theme(1L, "우테코", "방탈출", "https://");

            Reservation reservation1 = new Reservation(1L, "가이온", LocalDate.of(2025, 4, 24), reservationTime, theme);
            Reservation reservation2 = new Reservation(2L, "홍길동", LocalDate.of(2025, 4, 25), reservationTime, theme);

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>(List.of(reservation1, reservation2)));
            reservationService = new ReservationService(reservationRepository);

            List<ReservationResponse> responses = reservationService.findAllReservationResponses();

            assertThat(responses).hasSize(2);
            assertThat(responses).extracting("name").containsExactly("가이온", "홍길동");
        }
    }
}

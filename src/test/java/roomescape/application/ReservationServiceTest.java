package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.ReservationCommandRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;

@ServiceTest
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationCommandRepository reservationCommandRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void shouldReturnReservationResponsesWhenReservationsExist() {
        saveReservation();
        List<ReservationResponse> reservationResponses = reservationService.findAll();
        assertThat(reservationResponses).hasSize(1);
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하면 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationExist() {
        Reservation reservation = saveReservation();
        reservationService.deleteById(reservation.getId());

        List<Reservation> reservations = reservationQueryRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하지 않으면 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDoesNotExist() {
        assertThatCode(() -> reservationService.deleteById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 입니다.");
    }

    private Reservation saveReservation() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("test"), "test", "test"));
        Reservation reservation = new Reservation(new PlayerName("test"), LocalDate.of(2024, 1, 1), time, theme);
        return reservationCommandRepository.create(reservation);
    }
}

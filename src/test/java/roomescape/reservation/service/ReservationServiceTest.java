package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.ReservationSaveRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("존재하지 않는 시간에 예약을 하면 예외가 발생한다.")
    void emptyIdExceptionTest() {
        Long timeId = 1L;

        doReturn(Optional.empty()).when(reservationTimeRepository)
                .findById(timeId);

        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("hogi", LocalDate.now(), 1L, timeId);
        assertThatThrownBy(() -> reservationService.save(reservationSaveRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지나간 날짜를 예약 하면 예외가 발생한다")
    void beforeDateExceptionTest() {
        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("hogi",
                LocalDate.parse("1998-03-14"), 1L, 1L);

        assertThatThrownBy(() -> reservationService.save(reservationSaveRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중복된 예약이 있다면 예외가 발생한다.")
    void duplicateReservationExceptionTest() {
        Theme theme = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        doReturn(Optional.of(reservationTime)).when(reservationTimeRepository)
                .findById(1L);

        doReturn(Optional.of(theme)).when(themeRepository)
                .findById(1L);

        doReturn(true).when(reservationRepository)
                .existReservation(Mockito.any(Reservation.class));

        ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("hogi",
                LocalDate.parse("2025-03-14"), 1L, 1L);
        assertThatThrownBy(() -> reservationService.save(reservationSaveRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 시간에 예약 아이디일 경우 예외가 발생한다.")
    void findByIdExceptionTest() {
        Long reservationId = 1L;

        doReturn(Optional.empty()).when(reservationRepository)
                .findById(reservationId);

        assertThatThrownBy(() -> reservationService.findById(reservationId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeService themeService;

    @Mock
    private ReservationTimeService reservationTimeService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("지나간 날짜를 예약 하면 예외가 발생한다")
    void beforeDateExceptionTest() {
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest("hogi",
                LocalDate.parse("1998-03-14"), 1L, 1L);

        assertThatThrownBy(() -> reservationService.save(reservationCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중복된 예약이 있다면 예외가 발생한다.")
    void duplicateReservationExceptionTest() {
        Theme theme = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        doReturn(TimeResponse.toResponse(reservationTime)).when(reservationTimeService)
                .findById(1L);

        doReturn(ThemeResponse.toResponse(theme)).when(themeService)
                .findById(1L);

        doReturn(true).when(reservationTimeService)
                .isExist(1L);

        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest("hogi",
                LocalDate.parse("2025-03-14"), 1L, 1L);
        assertThatThrownBy(() -> reservationService.save(reservationCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

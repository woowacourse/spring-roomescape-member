package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dto.ReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.user.UserName;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("전체 예약 조회")
    void test1() {
        // given
        Reservation reservation1 = new Reservation(
                1L,
                new UserName("띠용"),
                new ReservationDateTime(LocalDate.now().minusDays(1), new ReservationTime(1L, LocalTime.of(11, 0))),
                new Theme(1L, "테마명1", "테마설명1", "테마썸네일링크1")
        );
        Reservation reservation2 = new Reservation(
                2L,
                new UserName("띠용투"),
                new ReservationDateTime(LocalDate.now().minusDays(1), new ReservationTime(1L, LocalTime.of(11, 0))),
                new Theme(2L, "테마명2", "테마설명2", "테마썸네일링크2")
        );
        List<Reservation> reservations = List.of(reservation1, reservation2);

        // when
        when(reservationRepository.getAllReservations()).thenReturn(reservations);
        List<Reservation> reservationsResult = reservationService.getAllReservations();

        // then
        assertThat(reservationsResult).containsExactly(reservation1, reservation2);
    }

    @Mock
    ReservationTimeService reservationTimeService;
    @Mock
    ThemeService themeService;
    @Mock
    ReservedChecker reservedChecker;

    @Test
    @DisplayName("예약 생성 조회")
    void test2() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(11, 0));
        Theme theme1 = new Theme(1L, "테마명1", "테마설명1", "테마썸네일링크1");
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);

        when(reservationTimeService.getReservationTimeById(1L)).thenReturn(reservationTime1);
        when(themeService.getThemeById(1L)).thenReturn(theme1);
        when(reservedChecker.contains(LocalDate.now().plusDays(1), 1L, 1L)).thenReturn(false);

        assertThatCode(() -> reservationService.addReservation(new UserName("유저명"),
                reservationRequest)).doesNotThrowAnyException();
    }

}

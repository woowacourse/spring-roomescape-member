package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약_가능한_시간_조회() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 10);
        Theme theme = new Theme(themeId, "우테코 방탈출", "우테코 방탈출", "s3.com");

        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationTime reservationTime3 = new ReservationTime(4L, LocalTime.of(16, 0));

        given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservationTime1, reservationTime2, reservationTime3));

        Reservation reservation = new Reservation(1L, "그해", theme, date, reservationTime1);

        given(reservationRepository.findByThemeAndDate(themeId, date))
                .willReturn(List.of(reservation));

        List<ReservationTimeResponse> availableTimes = reservationTimeService.readAvailableTimes(themeId, date);

        List<ReservationTimeResponse> expected = List.of(
                new ReservationTimeResponse(2L, LocalTime.of(11, 0)),
                new ReservationTimeResponse(4L, LocalTime.of(16, 0))
        );

        assertThat(availableTimes)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expected);
    }

    @Test
    void 예약_가능한_시간_없는_경우() {
        Long themeId = 5L;
        LocalDate date = LocalDate.of(2026, 5, 10);
        Theme theme = new Theme(themeId, "공포의 방", "무서운 방", "s3.com");

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservationTime));

        Reservation reservation = new Reservation(1L, "그해", theme, date, reservationTime);
        given(reservationRepository.findByThemeAndDate(themeId, date))
                .willReturn(List.of(reservation));

        List<ReservationTimeResponse> availableTimes = reservationTimeService.readAvailableTimes(themeId, date);

        assertThat(availableTimes).isEmpty();
    }
}

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

        ReservationTime time1 = new ReservationTime(1L,
                LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L,
                LocalTime.of(11, 0));
        ReservationTime time3 = new ReservationTime(3L,
                LocalTime.of(14, 0));
        ReservationTime time4 = new ReservationTime(4L,
                LocalTime.of(16, 0));
        ReservationTime time5 = new ReservationTime(5L,
                LocalTime.of(19, 0));

        Theme theme = new Theme(1L, "공포의 방", "설명", "thumb.jpg");

        given(reservationRepository.findByThemeAndDate(themeId, date))
                .willReturn(List.of(
                        new Reservation(1L, "동키", theme, date, time1, null),
                        new Reservation(2L, "그해", theme, date, time3, null),
                        new Reservation(3L, "아루", theme, date, time5, null)
                ));

        given(reservationTimeRepository.findAll())
                .willReturn(List.of(time1, time2, time3, time4,
                        time5));

        List<ReservationTimeResponse> availableTimes =
                reservationTimeService.readAvailableTimes(themeId,
                        date);

        assertThat(availableTimes).containsExactly(
                new ReservationTimeResponse(2L, LocalTime.of(11, 0)),
                new ReservationTimeResponse(4L, LocalTime.of(16, 0))
        );
    }

    @Test
    void 예약_가능한_시간_없는_경우() {
        Long themeId = 5L;
        LocalDate date = LocalDate.of(2026, 5, 10);

        ReservationTime time1 = new ReservationTime(1L,
                LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L,
                LocalTime.of(11, 0));
        ReservationTime time3 = new ReservationTime(3L,
                LocalTime.of(14, 0));
        ReservationTime time4 = new ReservationTime(4L,
                LocalTime.of(16, 0));
        ReservationTime time5 = new ReservationTime(5L,
                LocalTime.of(19, 0));

        Theme theme = new Theme(5L, "초보자 방", "설명", "thumb.jpg");

        given(reservationRepository.findByThemeAndDate(themeId, date))
                .willReturn(List.of(
                        new Reservation(1L, "동키", theme, date, time1, null),
                        new Reservation(2L, "그해", theme, date, time2, null),
                        new Reservation(3L, "아루", theme, date, time3, null),
                        new Reservation(4L, "매트", theme, date, time4, null),
                        new Reservation(5L, "동키", theme, date, time5, null)
                ));

        given(reservationTimeRepository.findAll())
                .willReturn(List.of(time1, time2, time3, time4,
                        time5));

        List<ReservationTimeResponse> availableTimes =
                reservationTimeService.readAvailableTimes(themeId,
                        date);

        assertThat(availableTimes).isEmpty();
    }
}

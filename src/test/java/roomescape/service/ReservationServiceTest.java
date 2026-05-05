package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository timeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private static final ReservationTime SAVED_TIME = new ReservationTime(1L, "12:30");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final Reservation RESERVATION = new Reservation(1L, "name", "2026-05-05", SAVED_TIME, SAVED_THEME);

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        ReservationTime availableTime1 = new ReservationTime(1L, "12:30");
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");

        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of(availableTime1, availableTime2));

        // when
        List<ReservationTime> availableTimes = reservationService.getAvailableTimes(RESERVATION.getDate(),
            RESERVATION.getThemeId());

        // then
        assertThat(availableTimes).hasSize(2);
        assertThat(availableTimes).extracting(ReservationTime::getId)
            .anySatisfy(id -> assertThat(id).isEqualTo(1L))
            .anySatisfy(id -> assertThat(id).isEqualTo(2L));

    }
}
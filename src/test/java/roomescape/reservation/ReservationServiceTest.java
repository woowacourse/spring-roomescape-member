package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.AlreadyInUseException;
import roomescape.exception.ForbiddenException;
import roomescape.exception.InvalidStateException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private ReservationService reservationService;

    private final Theme theme = new Theme(1L, "공포의 방", "무서운 방", "http://s3.com");
    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

    @Test
    void 이미_지난_날짜로_예약_생성시_400() {
        mockTime(LocalDate.of(2026, 5, 14), LocalTime.of(12, 0));

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        ReservationRequest request = new ReservationRequest("동키", 1L, LocalDate.of(2026, 5, 13), 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(InvalidStateException.class);
    }

    @Test
    void 이미_지난_날짜로_예약_변경시_400() {
        mockTime(LocalDate.of(2026, 5, 14), LocalTime.of(12, 0));

        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 5, 20), reservationTime);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        ReservationRequest request = new ReservationRequest("동키", 1L, LocalDate.of(2026, 5, 1), 1L);

        assertThatThrownBy(() -> reservationService.update(1L, request, "동키"))
                .isInstanceOf(InvalidStateException.class);
    }

    @Test
    void 이미_지난_예약_삭제시_400() {
        mockTime(LocalDate.of(2026, 5, 14), LocalTime.of(12, 0));

        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 5, 10), reservationTime);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.delete(1L, "동키"))
                .isInstanceOf(InvalidStateException.class);
    }

    @Test
    void 중복_예약_생성시_409() {
        mockTime(LocalDate.of(2026, 5, 14), LocalTime.of(12, 0));

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));
        given(reservationRepository.existsByThemeIdAndDateAndTimeId(anyLong(), any(LocalDate.class), anyLong()))
                .willReturn(true);

        ReservationRequest request = new ReservationRequest("동키", 1L, LocalDate.of(2026, 5, 20), 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(AlreadyInUseException.class);
    }

    @Test
    void 다른_사용자_예약_삭제시_403() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 5, 20), reservationTime);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.delete(1L, "그해"))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 다른_사용자_예약_변경시_403() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 5, 20), reservationTime);
        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        ReservationRequest request = new ReservationRequest("그해", 1L, LocalDate.of(2026, 5, 21), 1L);

        assertThatThrownBy(() -> reservationService.update(1L, request, "그해"))
                .isInstanceOf(ForbiddenException.class);
    }

    private void mockTime(LocalDate date, LocalTime time) {
        Instant fixedInstant = date.atTime(time).atZone(ZoneId.of("Asia/Seoul")).toInstant();
        given(clock.instant()).willReturn(fixedInstant);
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
    }
}

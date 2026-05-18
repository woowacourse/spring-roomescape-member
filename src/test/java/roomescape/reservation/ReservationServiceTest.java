package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.RoomescapeException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
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

    private ReservationService reservationService;

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-01-01T12:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository, reservationTimeRepository,
                themeRepository, fixedClock
        );
    }

    @Test
    void 예약_생성() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포의 방", "설명", "thumb.jpg");
        LocalDate date = LocalDate.of(2026, 12, 31);
        ReservationRequest request = new ReservationRequest("동키", 1L, date, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));
        given(reservationRepository.save(any()))
                .willReturn(new Reservation(1L, "동키", theme, date, time, null));

        ReservationResponse response = reservationService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.userName()).isEqualTo("동키");
    }

    @Test
    void 예약_시간이_없는_경우_예외() {
        ReservationRequest request = new ReservationRequest("동키", 1L,
                LocalDate.of(2026, 12, 31), 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 테마가_없는_경우_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationRequest request = new ReservationRequest("동키", 1L,
                LocalDate.of(2026, 12, 31), 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));
        given(themeRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 중복_예약시_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포의 방", "설명", "thumb.jpg");
        LocalDate date = LocalDate.of(2026, 5, 10);
        ReservationRequest request = new ReservationRequest("동키", 1L, date, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(time));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));
        given(reservationRepository.existsActiveByDateAndThemeAndTime(date, 1L, 1L))
                .willReturn(true);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 과거_시간_예약시_예외() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10,
                0));
        Theme theme = new Theme(1L, "공포의 방", "설명", "thumb.jpg");
        LocalDate pastDate = LocalDate.of(2025, 12, 31);
        ReservationRequest request = new ReservationRequest("동키", 1L,
                pastDate, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.
                of(time));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class);
    }
}

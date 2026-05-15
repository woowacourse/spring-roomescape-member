package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.reservation.InvalidReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.reservation.CreateReservationCommand;

class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-15T05:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        reservationTimeRepository = mock(ReservationTimeRepository.class);
        themeRepository = mock(ThemeRepository.class);
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                FIXED_CLOCK
        );
    }

    @Test
    void 지나간_날짜로_예약할_수_없다() {
        stubReservationDependencies(LocalTime.of(15, 0));

        CreateReservationCommand command = new CreateReservationCommand(
                "브라운",
                LocalDate.of(2026, 5, 14),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.createReservation(command))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("과거 날짜/시간으로는 예약할 수 없습니다.");
    }

    @Test
    void 예약_날짜가_오늘이면_현재_서버_시간_이전의_예약_시간은_선택할_수_없다() {
        stubReservationDependencies(LocalTime.of(13, 0));

        CreateReservationCommand command = new CreateReservationCommand(
                "브라운",
                LocalDate.of(2026, 5, 15),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.createReservation(command))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("과거 날짜/시간으로는 예약할 수 없습니다.");
    }

    @Test
    void 오늘_기준_30일을_초과한_날짜로_예약할_수_없다() {
        stubReservationDependencies(LocalTime.of(15, 0));

        CreateReservationCommand command = new CreateReservationCommand(
                "브라운",
                LocalDate.of(2026, 6, 15),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.createReservation(command))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("30일을 초과한 날짜로는 예약할 수 없습니다.");
    }

    private void stubReservationDependencies(LocalTime startAt) {
        ReservationTime time = ReservationTime.from(1L, startAt);
        Theme theme = Theme.from(1L, "테마", "설명", "/images/themes/theme.webp");
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
    }
}

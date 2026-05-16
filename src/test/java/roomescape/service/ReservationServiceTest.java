package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.reservation.DuplicateReservationException;
import roomescape.global.exception.reservation.ExpiredReservationException;
import roomescape.global.exception.reservation.InvalidReservationException;
import roomescape.global.exception.reservation.ReservationNotFoundException;
import roomescape.global.exception.reservation.SameReservationScheduleException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.reservation.CreateReservationCommand;
import roomescape.service.dto.reservation.CancelReservationCommand;
import roomescape.service.dto.reservation.ChangeReservationScheduleCommand;

class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-15T05:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
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
                "고래",
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
                "고래",
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
                "고래",
                LocalDate.of(2026, 6, 15),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.createReservation(command))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("30일을 초과한 날짜로는 예약할 수 없습니다.");
    }

    @Test
    void 같은_날짜_시간_테마에_이미_예약이_있으면_중복_예약을_거부한다() {
        stubReservationDependencies(LocalTime.of(15, 0));
        when(reservationTimeRepository.findReservedTimeIds(1L, LocalDate.of(2026, 5, 16)))
                .thenReturn(List.of(1L));

        CreateReservationCommand command = new CreateReservationCommand(
                "고래",
                LocalDate.of(2026, 5, 16),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.createReservation(command))
                .isInstanceOf(DuplicateReservationException.class);
    }

    @Test
    void 이미_예약된_슬롯으로_예약을_변경할_수_없다() {
        Reservation reservation = reservation();
        ReservationTime targetTime = ReservationTime.from(2L, LocalTime.of(11, 0));
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(2L)).thenReturn(Optional.of(targetTime));
        when(reservationTimeRepository.findReservedTimeIds(1L, LocalDate.of(2026, 5, 16)))
                .thenReturn(List.of(1L, 2L));

        ChangeReservationScheduleCommand command = new ChangeReservationScheduleCommand(
                1L,
                "고래",
                LocalDate.of(2026, 5, 16),
                2L
        );

        assertThatThrownBy(() -> reservationService.changeReservationSchedule(command))
                .isInstanceOf(DuplicateReservationException.class);
    }

    @Test
    void 이미_같은_일정으로_예약되어_있으면_변경할_수_없다() {
        Reservation reservation = reservation();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(reservation.getTime()));

        ChangeReservationScheduleCommand command = new ChangeReservationScheduleCommand(
                1L,
                "고래",
                LocalDate.of(2026, 5, 16),
                1L
        );

        assertThatThrownBy(() -> reservationService.changeReservationSchedule(command))
                .isInstanceOf(SameReservationScheduleException.class)
                .hasMessage("이미 같은 일정으로 예약되어 있습니다.");
    }

    @Test
    void 지난_예약은_변경할_수_없다() {
        Reservation reservation = pastReservation();
        ReservationTime targetTime = ReservationTime.from(2L, LocalTime.of(11, 0));
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(2L)).thenReturn(Optional.of(targetTime));

        ChangeReservationScheduleCommand command = new ChangeReservationScheduleCommand(
                1L,
                "고래",
                LocalDate.of(2026, 5, 16),
                2L
        );

        assertThatThrownBy(() -> reservationService.changeReservationSchedule(command))
                .isInstanceOf(ExpiredReservationException.class)
                .hasMessage("지난 예약은 변경할 수 없습니다.");
    }

    @Test
    void 예약자_이름이_일치하지_않으면_예약을_변경할_수_없다() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation()));

        ChangeReservationScheduleCommand command = new ChangeReservationScheduleCommand(
                1L,
                "라텔",
                LocalDate.of(2026, 5, 16),
                1L
        );

        assertThatThrownBy(() -> reservationService.changeReservationSchedule(command))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("해당 예약을 찾을 수 없습니다.");
    }

    @Test
    void 예약자_이름이_일치하지_않으면_예약을_취소할_수_없다() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation()));

        CancelReservationCommand command = new CancelReservationCommand(1L, "라텔");

        assertThatThrownBy(() -> reservationService.cancelReservation(command))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("해당 예약을 찾을 수 없습니다.");
    }

    @Test
    void 지난_예약은_취소할_수_없다() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(pastReservation()));

        CancelReservationCommand command = new CancelReservationCommand(1L, "고래");

        assertThatThrownBy(() -> reservationService.cancelReservation(command))
                .isInstanceOf(ExpiredReservationException.class)
                .hasMessage("지난 예약은 취소할 수 없습니다.");
    }

    private void stubReservationDependencies(LocalTime startAt) {
        ReservationTime time = ReservationTime.from(1L, startAt);
        Theme theme = Theme.from(1L, "테마", "설명", "/images/themes/theme.webp");
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
    }

    private Reservation reservation() {
        return Reservation.from(
                1L,
                "고래",
                LocalDate.of(2026, 5, 16),
                ReservationTime.from(1L, LocalTime.of(10, 0)),
                Theme.from(1L, "테마", "설명", "/images/themes/theme.webp"),
                ReservationStatus.RESERVED
        );
    }

    private Reservation pastReservation() {
        return Reservation.from(
                1L,
                "고래",
                LocalDate.of(2026, 5, 14),
                ReservationTime.from(1L, LocalTime.of(10, 0)),
                Theme.from(1L, "테마", "설명", "/images/themes/theme.webp"),
                ReservationStatus.RESERVED
        );
    }
}

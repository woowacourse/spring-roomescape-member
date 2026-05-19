package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.command.ReservationEditCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnprocessableException;
import roomescape.exception.code.ConflictCode;
import roomescape.exception.code.NotFoundCode;
import roomescape.exception.code.UnprocessableCode;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ReservationServiceEditBoundaryTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 5, 18);
    private static final LocalTime NOW_TIME = LocalTime.of(10, 0);
    private static final LocalDateTime NOW = LocalDateTime.of(TODAY, NOW_TIME);

    private static final long TIME_NOW_ID = 1L;
    private static final long TIME_LATER_ID = 2L;
    private static final long TIME_PAST_ID = 3L;
    private static final long THEME_ID = 1L;

    private static final ReservationTime TIME_NOW = new ReservationTime(TIME_NOW_ID, NOW_TIME);
    private static final ReservationTime TIME_LATER = new ReservationTime(TIME_LATER_ID, LocalTime.of(11, 0));
    private static final ReservationTime TIME_PAST = new ReservationTime(TIME_PAST_ID, NOW_TIME.minusSeconds(1));
    private static final Theme THEME = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");

    private List<Reservation> referencedReservations;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        referencedReservations = new ArrayList<>();
        FakeReservationRepository reservationRepository = new FakeReservationRepository(referencedReservations);
        FakeReservationTimeRepository timeRepository =
                new FakeReservationTimeRepository(List.of(TIME_NOW, TIME_LATER, TIME_PAST));
        service = new ReservationService(reservationRepository, timeRepository, mock(ThemeRepository.class));
    }

    @Test
    void 존재하지_않는_예약을_수정하면_404() {
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(1), TIME_NOW_ID);

        assertThatThrownBy(() -> service.editReservation(999L, command, NOW))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    void 시작_시각이_정확히_현재_시각인_예약은_수정_가능() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY, TIME_NOW, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(1), TIME_LATER_ID);

        assertThatCode(() -> service.editReservation(1L, command, NOW))
                .doesNotThrowAnyException();
    }

    @Test
    void 시작_시각이_현재보다_빠른_예약은_수정_불가() {
        ReservationTime almostNow = new ReservationTime(99L, NOW_TIME.minusSeconds(1));
        referencedReservations.add(new Reservation(1L, "브라운", TODAY, almostNow, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(1), TIME_LATER_ID);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage(UnprocessableCode.RESERVATION_ALREADY_STARTED.getMessage());
    }

    @Test
    void 존재하지_않는_시간id로_수정시_404() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(2), 999L);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundCode.RESERVATION_TIME_NOT_FOUND.getMessage());
    }

    @Test
    void 오늘_날짜로_수정가능() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY, TIME_LATER_ID);

        assertThatCode(() -> service.editReservation(1L, command, NOW))
                .doesNotThrowAnyException();
    }

    @Test
    void 어제_날짜로_수정하면_예외() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.minusDays(1), TIME_LATER_ID);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage(UnprocessableCode.RESERVATION_PAST_DATE.getMessage());
    }

    @Test
    void 오늘_현재_시각으로_수정가능() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY, TIME_NOW_ID);

        assertThatCode(() -> service.editReservation(1L, command, NOW))
                .doesNotThrowAnyException();
    }

    @Test
    void 오늘_현재보다_1초_빠른_시간으로_수정하면_예외() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY, TIME_PAST_ID);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(UnprocessableException.class)
                .hasMessage(UnprocessableCode.RESERVATION_PAST_TIME.getMessage());
    }

    @Test
    void 다른_예약이_같은_슬롯을_차지하면_409() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        referencedReservations.add(new Reservation(2L, "조이", TODAY.plusDays(2), TIME_NOW, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(2), TIME_NOW_ID);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void 동일한_날짜_시간_테마로_수정_요청은_허용안됨() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(1), TIME_LATER_ID);

        assertThatThrownBy(() -> service.editReservation(1L, command, NOW))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ConflictCode.RESERVATION_DUPLICATED.getMessage());
    }

    @Test
    void 빈_슬롯으로_수정하면_성공하고_결과에_새_값이_반영() {
        referencedReservations.add(new Reservation(1L, "브라운", TODAY.plusDays(1), TIME_LATER, THEME));
        ReservationEditCommand command = new ReservationEditCommand(TODAY.plusDays(2), TIME_NOW_ID);

        Reservation result = service.editReservation(1L, command, NOW);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.date()).isEqualTo(TODAY.plusDays(2));
        assertThat(result.timeId()).isEqualTo(TIME_NOW_ID);
    }
}

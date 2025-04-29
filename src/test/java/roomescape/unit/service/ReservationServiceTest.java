package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.dto.AddReservationDto;
import roomescape.dto.AddReservationTimeDto;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationServiceTest {

    static ReservationService reservationService;
    static ReservationTimeService reservationTimeService;

    @BeforeEach
    void setup() {
        reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository());
        reservationService = new ReservationService(new FakeReservationRepository(), reservationTimeService);
    }

    @Test
    void 예약을_추가하고_조회할_수_있다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));

        assertThat(reservationService.allReservations().size()).isEqualTo(0);
        reservationService.addReservation(
                new AddReservationDto("praisebak", LocalDate.now().plusDays(1L), reservationTimeId));
        assertThat(reservationService.allReservations().size()).isEqualTo(1);
    }

    @Test
    void 이전_날짜에_예약할_수_없다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));
        Assertions.assertThatThrownBy(() -> reservationService.addReservation(
                        new AddReservationDto("투다", LocalDate.now().minusDays(1), reservationTimeId)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은날짜일시_이전_시간에_예약할_수_없다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().minusHours(1L)));
        Assertions.assertThatThrownBy(() -> reservationService.addReservation(
                        new AddReservationDto("투다", LocalDate.now(), reservationTimeId)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이후_날짜에_예약할_수_있다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));
        Assertions.assertThatCode(() -> reservationService.addReservation(
                        new AddReservationDto("투다", LocalDate.now().plusDays(1), reservationTimeId)))
                .doesNotThrowAnyException();
    }

    @Test
    void 같은날짜일시_이후_시간_예약할_수_있다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));
        Assertions.assertThatCode(() -> reservationService.addReservation(
                        new AddReservationDto("투다", LocalDate.now(), reservationTimeId)))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_삭제하고_조회할_수_있다() {
        Long reservationTimeId = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusHours(1L)));

        long id = reservationService.addReservation(
                new AddReservationDto("praisebak", LocalDate.now().plusDays(1L), reservationTimeId));
        assertThat(reservationService.allReservations().size()).isEqualTo(1);
        reservationService.deleteReservation(id);
        assertThat(reservationService.allReservations().size()).isEqualTo(0);
    }
}

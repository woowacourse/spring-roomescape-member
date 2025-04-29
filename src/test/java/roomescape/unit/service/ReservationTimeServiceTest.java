package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.AddReservationTimeDto;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.ReservationTimeService;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeServiceTest {

    static ReservationTimeService reservationTimeService;

    static ReservationRepository reservationRepository;
    static ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationRepository,
                reservationTimeRepository);
    }

    @Test
    void 예약시간을_추가하고_조회할_수_있다() {
        reservationTimeService.addReservationTime(new AddReservationTimeDto(LocalTime.now().plusMinutes(30L)));
        assertThat(reservationTimeService.allReservationTimes().size()).isEqualTo(1);
    }

    @Test
    void 예약시간을_삭제하고_조회할_수_있다() {
        long id = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(LocalTime.now().plusMinutes(30L)));

        int before = reservationTimeService.allReservationTimes().size();
        reservationTimeService.deleteReservationTime(id);
        int after = reservationTimeService.allReservationTimes().size();

        assertAll(() -> {
            assertThat(before).isEqualTo(1);
            assertThat(after).isEqualTo(0);
        });
    }

    @Test
    void 특정_시간에_대한_예약이_존재할때_시간을_삭제하려고하면_예외가_발생한다() {
        LocalTime startAt = LocalTime.now().plusMinutes(30L);
        Long id = reservationTimeService.addReservationTime(
                new AddReservationTimeDto(startAt));
        ReservationTime reservationTime = new ReservationTime(id, startAt);
        Reservation reservation = new Reservation(null, "praisebak", LocalDate.now().plusDays(1), reservationTime);
        reservationRepository.add(reservation);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;
import roomescape.repository.ReservationFakeRepository;
import roomescape.repository.TimeSlotFakeRepository;

class TimeSlotServiceTest {

    private TimeSlotService service;

    @BeforeEach
    void setUp() {
        service = new TimeSlotService(new ReservationFakeRepository(),
            new TimeSlotFakeRepository());
    }

    @Test
    @DisplayName("예약 시간을 추가할 수 있다.")
    void addTimeSlot() {
        // given
        var startAt = LocalTime.of(10, 0);

        // when
        TimeSlot created = service.add(startAt);

        // then
        var timeSlots = service.allTimeSlots();
        assertThat(timeSlots).contains(created);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void deleteTimeSlot() {
        // given
        var startAt = LocalTime.of(10, 0);
        var target = service.add(startAt);

        // when
        boolean isRemoved = service.removeById(target.id());

        // then
        var timeSlots = service.allTimeSlots();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(timeSlots).doesNotContain(target)
        );
    }

    @Test
    @DisplayName("예약 시간을 삭제할 때 해당 시간에 대한 예약이 존재하면 예외 발생")
    void deleteTimeSlotWithReservation() {
        // given
        var reservationRepository = new ReservationFakeRepository();
        var timeSlotService = new TimeSlotService(reservationRepository,
            new TimeSlotFakeRepository());

        var startAt = LocalTime.of(10, 0);
        var timeSlot = timeSlotService.add(startAt);

        var name = "포포";
        var date = LocalDate.of(2024, 4, 18);
        var reservation = new Reservation(null, name, date, timeSlot);
        reservationRepository.save(reservation);

        // when & then
        assertThatThrownBy(() -> timeSlotService.removeById(timeSlot.id()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("삭제하려는 예약 시간을 사용하는 예약이 있습니다.");
    }
}

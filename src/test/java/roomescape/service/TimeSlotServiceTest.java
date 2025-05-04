package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.Fixtures.JUNK_TIME_SLOT_REQUEST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.Fixtures;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.repository.fake.ReservationFakeRepository;
import roomescape.repository.fake.TimeSlotFakeRepository;

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
        // given & when
        TimeSlotResponse response = service.add(JUNK_TIME_SLOT_REQUEST);

        // then
        var timeSlots = service.findAll();
        assertThat(timeSlots).contains(response);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void deleteTimeSlot() {
        // given
        var response = service.add(JUNK_TIME_SLOT_REQUEST);

        // when
        boolean isRemoved = service.removeById(response.id());

        // then
        var timeSlots = service.findAll();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(timeSlots).doesNotContain(response)
        );
    }

    @Test
    @DisplayName("예약 시간을 삭제할 때 해당 시간에 대한 예약이 존재하면 예외 발생")
    void deleteTimeSlotWithReservation() {
        // given
        var reservationRepository = new ReservationFakeRepository();
        var timeSlotService = new TimeSlotService(reservationRepository,
            new TimeSlotFakeRepository());

        var timeSlotResponse = timeSlotService.add(JUNK_TIME_SLOT_REQUEST);

        var reservation = Fixtures.getReservationOfTomorrow();
        reservationRepository.save(reservation);

        // when & then
        assertThatThrownBy(() -> timeSlotService.removeById(timeSlotResponse.id()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("삭제하려는 타임 슬롯을 사용하는 예약이 있습니다.");
    }
}

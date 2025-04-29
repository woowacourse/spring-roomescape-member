package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.TimeSlot;
import roomescape.repository.TimeSlotFakeRepository;

class TimeSlotServiceTest {

    private TimeSlotService service;

    @BeforeEach
    void setUp() {
         service = new TimeSlotService(new TimeSlotFakeRepository());
    }

    @Test
    @DisplayName("예약 시간을 추가할 수 있다.")
    void addTimeSlot() {
        //given
        var startAt = LocalTime.of(10, 0);

        //when
        TimeSlot created = service.add(startAt);
        System.out.println("created = " + created);

        //then
        var timeSlots = service.allTimeSlots();
        assertThat(timeSlots).contains(created);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void deleteTimeSlot() {
        //given
        var startAt = LocalTime.of(10, 0);
        var target = service.add(startAt);

        //when
        boolean isRemoved = service.removeById(target.id());

        //then
        var timeSlots = service.allTimeSlots();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(timeSlots).doesNotContain(target)
        );
    }
}

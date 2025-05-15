package roomescape.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.service.ReservationTimeService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeServiceTest(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @Test
    void saveReservationTimeTest() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10,0));

        // when
        reservationTimeService.saveReservationTime(request);

        // then
        List<ReservationTime> reservationTimes = reservationTimeService.readReservationTime();
        Assertions.assertAll(
                () -> assertThat(reservationTimes).hasSize(1),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo("10:00")
        );
    }

    @Test
    void readReservationTimeTest() {
        // given
        ReservationTimeRequest request1 = new ReservationTimeRequest(LocalTime.of(10,0));
        ReservationTimeRequest request2 = new ReservationTimeRequest(LocalTime.of(11,0));
        reservationTimeService.saveReservationTime(request1);
        reservationTimeService.saveReservationTime(request2);

        // when
        List<ReservationTime> reservationTimes = reservationTimeService.readReservationTime();

        // then
        Assertions.assertAll(
                () -> assertThat(reservationTimes).hasSize(2),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo("10:00"),
                () -> assertThat(reservationTimes.get(1).getStartAt()).isEqualTo("11:00")
        );
    }

    @Test
    void deleteReservationTimeTest() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10,0));
        reservationTimeService.saveReservationTime(request);
        List<ReservationTime> reservationTimesBeforeDelete = reservationTimeService.readReservationTime();

        // when
        reservationTimeService.deleteReservationTime(reservationTimesBeforeDelete.get(0).getId());
        List<ReservationTime> reservationTimesAfterDelete = reservationTimeService.readReservationTime();

        // then
        Assertions.assertAll(
                () -> assertThat(reservationTimesBeforeDelete).hasSize(1),
                () -> assertThat(reservationTimesAfterDelete).isEmpty()
        );
    }

    @Test
    void readAvailableTimesByTest() {
        // given
        ReservationTimeRequest request1 = new ReservationTimeRequest(LocalTime.of(10,0));
        ReservationTimeRequest request2 = new ReservationTimeRequest(LocalTime.of(11,0));
        reservationTimeService.saveReservationTime(request1);
        reservationTimeService.saveReservationTime(request2);

        // when
        List<ReservationTimeResponseWithBookedStatus> availableTimes = reservationTimeService.readAvailableTimesBy(LocalDate.now(), 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(availableTimes).hasSize(2),
                () -> assertThat(availableTimes.get(0).booked()).isFalse(),
                () -> assertThat(availableTimes.get(1).booked()).isFalse()
        );
    }

}


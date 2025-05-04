package roomescape.service;

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
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;

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
    void createReservationTimeTest() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10,0));

        // when
        reservationTimeService.createReservationTime(request);

        // then
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();
        Assertions.assertAll(
                () -> assertThat(reservationTimes).hasSize(1),
                () -> assertThat(reservationTimes.get(0).startAt()).isEqualTo("10:00")
        );
    }

    @Test
    void findAllTest() {
        // given
        ReservationTimeCreateRequest request1 = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        ReservationTimeCreateRequest request2 = new ReservationTimeCreateRequest(LocalTime.of(11,0));
        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);

        // when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        // then
        Assertions.assertAll(
                () -> assertThat(reservationTimes).hasSize(2),
                () -> assertThat(reservationTimes.get(0).startAt()).isEqualTo("10:00"),
                () -> assertThat(reservationTimes.get(1).startAt()).isEqualTo("11:00")
        );
    }

    @Test
    void deleteReservationTimeByIdTest() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        reservationTimeService.createReservationTime(request);
        List<ReservationTimeResponse> reservationTimesBeforeDelete = reservationTimeService.findAll();

        // when
        reservationTimeService.deleteReservationTimeById(reservationTimesBeforeDelete.get(0).id());
        List<ReservationTimeResponse> reservationTimesAfterDelete = reservationTimeService.findAll();

        // then
        Assertions.assertAll(
                () -> assertThat(reservationTimesBeforeDelete).hasSize(1),
                () -> assertThat(reservationTimesAfterDelete).isEmpty()
        );
    }

    @Test
    void findAvailableReservationTimesByDateAndThemeIdTest() {
        // given
        ReservationTimeCreateRequest request1 = new ReservationTimeCreateRequest(LocalTime.of(10,0));
        ReservationTimeCreateRequest request2 = new ReservationTimeCreateRequest(LocalTime.of(11,0));
        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);

        // when
        List<ReservationTimeResponseWithBookedStatus> availableTimes = reservationTimeService.findAvailableReservationTimesByDateAndThemeId(LocalDate.now(), 1L);

        // then
        Assertions.assertAll(
                () -> assertThat(availableTimes).hasSize(2),
                () -> assertThat(availableTimes.get(0).booked()).isFalse(),
                () -> assertThat(availableTimes.get(1).booked()).isFalse()
        );
    }

}


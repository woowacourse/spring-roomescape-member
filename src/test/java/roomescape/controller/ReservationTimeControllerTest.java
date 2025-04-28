package roomescape.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.ResponseEntity;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.service.ReservationTimeService;

class ReservationTimeControllerTest {
    private final ReservationTimeService fakeReservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository());
    private final ReservationTimeController reservationTimeController = new ReservationTimeController(fakeReservationTimeService);

    @ParameterizedTest
    @CsvSource(value = {"18:00"})
    void create(LocalTime startAt) {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);

        // when
        ResponseEntity<ReservationTime> actualResponse = reservationTimeController.create(reservationTimeRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> Assertions.assertThat(actualResponse.getBody().getStartAt()).isEqualTo(reservationTimeRequest.startAt())
        );
    }

    @Test
    void read() {
        // given
        LocalTime givenLocalTime = LocalTime.now();
        reservationTimeController.create(new ReservationTimeRequest(givenLocalTime));

        // when
        List<ReservationTime> actualResponse = reservationTimeController.read().getBody();

        // then
        Assertions.assertThat(actualResponse).hasSize(1);
        Assertions.assertThat(actualResponse.getFirst().getStartAt()).isEqualTo(givenLocalTime);
    }

    @Test
    void delete() {
        // given
        LocalTime givenLocalTime = LocalTime.now();
        reservationTimeController.create(new ReservationTimeRequest(givenLocalTime));

        // when
        reservationTimeController.delete(1L);

        // then
        List<ReservationTime> actualResponse = reservationTimeController.read().getBody();
        Assertions.assertThat(actualResponse).isEmpty();
    }
}

package roomescape.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.ResponseEntity;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.service.ReservationService;

class ReservationControllerTest {
    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    private final ReservationController reservationController = new ReservationController(new ReservationService(fakeReservationRepository, fakeReservationTimeRepository));

    private void makeStubReservationTime(LocalTime startAt) {
        fakeReservationTimeRepository.saveReservationTime(new ReservationTime(null, startAt));
    }

    @ParameterizedTest
    @CsvSource(value = {"히스타, 2002-12-02, 18:00"})
    void create(String nickname, LocalDate date, LocalTime time) {
        // given
        makeStubReservationTime(time);
        ReservationRequest reservationRequest = new ReservationRequest(nickname, date, 1L);

        // when
        ResponseEntity<ReservationResponse> actualResponse = reservationController.create(reservationRequest);

        // then
        Assertions.assertThat(actualResponse).isNotNull();
        Assertions.assertThat(actualResponse.getBody()).isNotNull();
        assertAll(
                () -> Assertions.assertThat(actualResponse.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> Assertions.assertThat(actualResponse.getBody().name()).isEqualTo(reservationRequest.name()),
                () -> Assertions.assertThat(actualResponse.getBody().date()).isEqualTo(reservationRequest.date()),
                () -> Assertions.assertThat(actualResponse.getBody().reservationTime().getId()).isEqualTo(1L),
                () -> Assertions.assertThat(actualResponse.getBody().reservationTime().getStartAt()).isEqualTo(time)
        );
    }

    @Test
    void read() {
        // given
        LocalDate givenDate = LocalDate.now();
        LocalTime givenTime = LocalTime.now();
        String givenName = "히스타";

        makeStubReservationTime(givenTime);
        ReservationRequest reservationRequest = new ReservationRequest(givenName, givenDate, 1L);
        reservationController.create(reservationRequest);

        // when
        ResponseEntity<List<ReservationResponse>> actualResponse = reservationController.read();

        // then
        List<ReservationResponse> reservationList = actualResponse.getBody();

        Assertions.assertThat(reservationList).hasSize(1);
        assertAll(
                () -> Assertions.assertThat(reservationList.getFirst().name()).isEqualTo(givenName),
                () -> Assertions.assertThat(reservationList.getFirst().date()).isEqualTo(givenDate),
                () -> Assertions.assertThat(reservationList.getFirst().reservationTime().getId()).isEqualTo(1L),
                () -> Assertions.assertThat(reservationList.getFirst().reservationTime().getStartAt()).isEqualTo(givenTime)
        );
    }

    @Test
    void delete() {
        // given
        makeStubReservationTime(LocalTime.now());
        ReservationRequest reservationRequest = new ReservationRequest("히스타", LocalDate.now(), 1L);
        reservationController.create(reservationRequest);


        // when
        reservationController.delete(1L);

        // then
        List<ReservationResponse> actualResponse = reservationController.read().getBody();
        Assertions.assertThat(actualResponse).isEmpty();
    }
}

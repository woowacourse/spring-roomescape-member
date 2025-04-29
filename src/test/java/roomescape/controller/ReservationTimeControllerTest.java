package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.ReservationTimeFixture.addReservationTimeInRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.ReservationTimeService;
import roomescape.test.fake.FakeReservationRepository;
import roomescape.test.fake.FakeReservationTimeRepository;

class ReservationTimeControllerTest {

    private static final LocalDate NEXT_DATE = LocalDate.now().plusDays(1);

    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeReservationTimeRepository();
    private final ReservationTimeService timeService =
            new ReservationTimeService(reservationRepository, timeRepository);
    private final ReservationTimeController controller = new ReservationTimeController(timeService);

    @DisplayName("등록된 모든 예약 가능 시간을 조회활 수 있다")
    @Test
    void canGetReservationTimes() {
        addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(11, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(12, 0));

        List<ReservationTime> responseBody = controller.getReservationTimes();

        assertThat(responseBody).hasSize(3);
    }

    @DisplayName("예약 가능 시간을 추가할 수 있다")
    @Test
    void canCreateReservationTime() {
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(LocalTime.of(10, 0));

        ResponseEntity<ReservationTime> response = controller.createReservationTime(request);

        ReservationTime savedTime = timeRepository.findAll().getFirst();
        ReservationTime expectedSavedTime = new ReservationTime(1L, request.getStartAt());
        assertAll(
                () -> assertThat(savedTime).isEqualTo(expectedSavedTime),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("times/1"),
                () -> assertThat(response.getBody()).isEqualTo(expectedSavedTime)
        );
    }

    @DisplayName("이미 추가한 시간의 경우 추가할 수 없다")
    @Test
    void canCreateSameReservationTime() {
        LocalTime sameStartAt = LocalTime.of(10, 0);
        addReservationTimeInRepository(timeRepository, sameStartAt);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(sameStartAt);

        assertThatThrownBy(() -> controller.createReservationTime(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[Error] 이미 추가가 완료된 예약 가능 시간입니다.");
    }

    @DisplayName("ID를 통해 예약 가능 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(11, 0));
        addReservationTimeInRepository(timeRepository, LocalTime.of(12, 0));

        ResponseEntity<Void> response = controller.deleteReservationTime(1L);

        assertAll(
                () -> assertThat(timeRepository.findAll()).extracting(ReservationTime::getId).doesNotContain(1L),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT)
        );
    }

    @DisplayName("존재하지 않는 예약 가능 시간을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void canNotDeleteWithInvalidId() {
        long noneExistentId = 1L;
        assertThatThrownBy(() -> controller.deleteReservationTime(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("이미 해당 시간에 예약이 존재하는 경우 예약을 제거할 수 없다")
    @Test
    void canNotDeleteBecauseReservations() {
        ReservationTime savedTime = addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        reservationRepository.add(Reservation.createWithoutId("reservation2", NEXT_DATE, savedTime));

        assertThatThrownBy(() -> controller.deleteReservationTime(savedTime.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 해당 시간에 대한 예약 데이터들이 존재합니다.");
    }
}

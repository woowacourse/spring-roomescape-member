package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @Test
    void createReservationTime_shouldThrowException_IfDuplicated() {
        LocalTime time = LocalTime.of(1, 1);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        reservationTimeService.create(request);

        assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createReservationTime_shouldReturnResponse_WhenSuccessful() {
        LocalTime time = LocalTime.of(9, 0);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(time);

        ReservationTimeResponse response = reservationTimeService.create(request);

        assertThat(response.startAt()).isEqualTo(time);
    }

    @Test
    void getReservationTimes_shouldReturnAllCreatedTimes() {
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ReservationTimeCreateRequest(LocalTime.of(11, 0)));

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteReservationTime_shouldThrowException_WhenIdNotFound() {
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요청한 id와 일치하는 예약 시간 정보가 없습니다.");
    }

    @Test
    void deleteReservationTime_shouldRemoveSuccessfully() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(13, 30));
        ReservationTimeResponse response = reservationTimeService.create(request);

        reservationTimeService.delete(response.id());

        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();
        assertThat(result).isEmpty();
    }
}
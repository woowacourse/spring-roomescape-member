package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.DuplicateSaveException;

class ReservationTimeServiceTest {

    @DisplayName("모든 예약 시간을 찾습니다")
    @Test
    void findAllReservationTime() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        ));
        int expectedSize = 1;

        int actualSize = reservationTimeService.findAllReservationTime().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @DisplayName("예약시간을 추가하고 저장된 예약시간을 반환합니다.")
    @Test
    void should_add_reservation_time() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository());
        ReservationTimeResponse expectedResponse = new ReservationTimeResponse(1L, LocalTime.of(10, 0));

        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(LocalTime.of(10, 0));

        ReservationTimeResponse actualReservationTime = reservationTimeService.saveReservationTime(
                reservationTimeAddRequest);

        assertThat(actualReservationTime).isEqualTo(expectedResponse);
    }

    @DisplayName("중복되는 예약 시각을 추가할 경우 예외가 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reservation_time_is_duplicated() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        ));

        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.saveReservationTime(reservationTimeAddRequest))
                .isInstanceOf(DuplicateSaveException.class);
    }

    @DisplayName("원하는 id의 예약시간을 삭제합니다")
    @Test
    void should_remove_reservation_time_with_exist_id() {
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        reservationTimeService.removeReservationTime(1L);

        assertThat(fakeReservationTimeDao.reservationTimes.containsKey(1L)).isFalse();
    }
}

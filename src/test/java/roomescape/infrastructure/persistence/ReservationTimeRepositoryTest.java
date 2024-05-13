package roomescape.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.ReservationTime;
import roomescape.service.response.AvailableReservationTimeResponse;
import roomescape.support.IntegrationTestSupport;

class ReservationTimeRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ReservationTimeRepository target;

    @Test
    @DisplayName("모든 예약 시간 데이터를 가져온다.")
    void findAll() {
        List<ReservationTime> reservationTimes = target.findAll();

        assertThat(reservationTimes).hasSize(3);
    }

    @Test
    @DisplayName("예약 시간 데이터가 존재하지 않으면 빈 리스트를 반환한다.")
    void empty() {
        cleanUp("reservation");
        cleanUp("reservation_time");

        List<ReservationTime> reservationTimes = target.findAll();

        assertThat(reservationTimes).isEmpty();
    }

    @Test
    @DisplayName("특정 예약 시간 id의 데이터를 조회한다.")
    void findById() {
        Optional<ReservationTime> findReservationTime = target.findById(예약_시간_2번_ID);

        assertThat(findReservationTime)
                .map(ReservationTime::getStartAt)
                .isNotEmpty()
                .get()
                .isEqualTo(LocalTime.parse("11:00"));
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 id의 데이터를 조회한다.")
    void notFound() {
        Optional<ReservationTime> findReservationTime = target.findById(0L);

        assertThat(findReservationTime).isEmpty();
    }

    @Test
    @DisplayName("새로운 예약 시간 데이터를 저장한다.")
    void create() {
        LocalTime startAt = LocalTime.parse("13:00");
        ReservationTime reservationTime = new ReservationTime(startAt);

        target.save(reservationTime);

        int countRow = countRow("reservation_time");
        assertThat(countRow).isEqualTo(4);
    }

    @Test
    @DisplayName("특정 id를 가진 예약 시간을 삭제한다.")
    void delete() {
        target.removeById(예약_시간_3번_ID);

        int countRow = countRow("reservation_time");
        assertThat(countRow).isEqualTo(2);
    }

    @Test
    @DisplayName("동일한 예약 시간이 있는지 확인한다.")
    void hasDuplicateTime() {
        LocalTime startAt = LocalTime.parse("10:00");
        ReservationTime reservationTime = new ReservationTime(startAt);

        boolean result = target.hasDuplicateTime(reservationTime);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("동일한 예약 시간이 없는지 확인한다.")
    void noHasDuplicateTime() {
        LocalTime startAt = LocalTime.parse("18:00");
        ReservationTime reservationTime = new ReservationTime(startAt);

        boolean result = target.hasDuplicateTime(reservationTime);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 날짜와 테마에 대한 사용 가능 시간을 조회할 수 있다.")
    void findAvailableTimes() {
        LocalDate date = LocalDate.parse("2023-05-04");

        List<AvailableReservationTimeResponse> result = target.findAvailableReservationTimes(date, 테마_1번_ID);

        AvailableReservationTimeResponse expectedAvailableTime =
                new AvailableReservationTimeResponse(예약_시간_3번_ID, "13:00", false);
        assertThat(result)
                .contains(expectedAvailableTime)
                .extracting(AvailableReservationTimeResponse::alreadyBooked)
                .containsExactly(true, true, false);
    }
}

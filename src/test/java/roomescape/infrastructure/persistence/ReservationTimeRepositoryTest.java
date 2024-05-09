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

/*
 * 테스트 데이터베이스 시간 초기 데이터
 * {ID=1, START_AT=10:00}
 * {ID=2, START_AT=11:00}
 * {ID=3, START_AT=13:00}
 */
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
        Optional<ReservationTime> findReservationTime = target.findById(2L);

        assertThat(findReservationTime)
                .map(ReservationTime::getStartAt)
                .isNotEmpty()
                .get()
                .isEqualTo(LocalTime.parse("11:00"));
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 id의 데이터를 조회한다.")
    void notFound() {
        Optional<ReservationTime> findReservationTime = target.findById(5L);

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
        target.removeById(3L);

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

    /*
     * 테스트 데이터베이스 예약 초기 데이터
     * {ID=1, NAME=브라운, DATE=2023-05-04, TIME={ID=1, START_AT="10:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
     * {ID=2, NAME=엘라, DATE=2023-05-04, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
     * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
     */
    @Test
    @DisplayName("주어진 날짜와 테마에 대한 사용 가능 시간을 조회할 수 있다.")
    void findAvailableTimes() {
        LocalDate date = LocalDate.parse("2023-05-04");
        Long themeId = 1L;

        List<AvailableReservationTimeResponse> result = target.findAvailableReservationTimes(date, themeId);

        AvailableReservationTimeResponse expectedAvailableTime =
                new AvailableReservationTimeResponse(3L, "13:00", false);
        assertThat(result)
                .contains(expectedAvailableTime)
                .extracting(AvailableReservationTimeResponse::alreadyBooked)
                .containsExactly(true, true, false);
    }
}

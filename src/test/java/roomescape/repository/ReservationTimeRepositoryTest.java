package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.repository.reservationTime.JdbcReservationTimeRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTimeRepositoryTest extends BaseRepositoryTest {
    private ReservationTimeRepository reservationTimeRepository;

    @Override
    protected void initTable() {
        createReservationTimeTable();
        createThemeTable();
        createReservationTable();

        insertReservationTime(LocalTime.parse("10:00"));

        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationTable();
        deleteReservationTimeTable();
        deleteThemeTable();
    }

    @Test
    @DisplayName("특정 예약 시간 정상적으로 가져오는 지 테스트")
    void getReservationTimeTest() {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.getReservationTime(1);

        assertThat(reservationTime.isPresent()).isTrue();
        assertThat(reservationTime.get()).isEqualTo(new ReservationTime(1L, LocalTime.parse("10:00")));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 시간 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationTimeTest() {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.getReservationTime(3);

        assertThat(reservationTime.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약시간 정상적으로 가져오는 지 테스트")
    void getReservationTimesTest() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.getAllReservationTime();

        assertThat(reservationTimes).containsExactly(new ReservationTime(1L, LocalTime.parse("10:00")));
    }

    @Test
    @DisplayName("예약 시간 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationTimeRepository.deleteReservationTime(1);
        List<ReservationTime> reservationTimes = reservationTimeRepository.getAllReservationTime();

        assertThat(reservationTimes).isNotIn(new ReservationTime(1L, LocalTime.parse("10:00")));
    }

    @Test
    @DisplayName("예약 시간 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {

        reservationTimeRepository.addReservationTime(new ReservationTime(LocalTime.parse("12:00")));

        List<ReservationTime> reservations = reservationTimeRepository.getAllReservationTime();

        ReservationTime expectedReservation = new ReservationTime(2L, LocalTime.parse("12:00"));

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations).contains(expectedReservation);
    }

    @Test
    @DisplayName("사용 가능한 예약 시간 반환 정상적으로 작동하는 지 테스트")
    void getReservationTimeByDateAndThemeTest() {
        insertReservationTime(LocalTime.parse("11:00"));

        insertTheme("테마1", "테마 설명", "image url");
        insertTheme("테마2", "테마 설명", "image url");

        insertReservation("브라운", LocalDate.parse("2023-08-03"), 1, 1);

        List<ReservationTimeWithAvailable> reservationTimeWithAvailable1 = reservationTimeRepository.getAvailableReservationTimeByDateAndTheme(new ReservationTimeCondition(LocalDate.parse("2023-08-04"), 1));
        List<ReservationTimeWithAvailable> reservationTimeWithAvailable2 = reservationTimeRepository.getAvailableReservationTimeByDateAndTheme(new ReservationTimeCondition(LocalDate.parse("2023-08-03"), 1));

        assertThat(reservationTimeWithAvailable1).containsAll(List.of(
                new ReservationTimeWithAvailable(1, LocalTime.parse("10:00"), true),
                new ReservationTimeWithAvailable(2, LocalTime.parse("11:00"), true)
        ));

        assertThat(reservationTimeWithAvailable2).containsAll(List.of(
                new ReservationTimeWithAvailable(1, LocalTime.parse("10:00"), false),
                new ReservationTimeWithAvailable(2, LocalTime.parse("11:00"), true)
        ));
    }
}

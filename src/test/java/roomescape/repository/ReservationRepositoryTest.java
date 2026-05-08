package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;

public class ReservationRepositoryTest extends BaseRepositoryTest {
    private ReservationRepository reservationRepository;

    private final Reservation INIT_RESERVATION = new Reservation(
            1,
            "브라운",
            "2023-08-05",
            new ReservationTime(1, "10:00"),
            new ReservationTheme(1, "테마1", "테마 설명", "image url")
    );

    @Override
    protected void initTable() {
        createReservationTimeTable();
        createReservationThemeTable();
        createReservationTable();

        insertReservationTime("10:00");
        insertReservationTheme("테마1", "테마 설명", "image url");
        insertReservation("브라운", "2023-08-05", 1, 1);
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationTable();
        deleteReservationTimeTable();
    }

    @Test
    @DisplayName("전체 예약 테스트 정상적으로 가져오는 지 테스트")
    void getReservationTest() {
        List<Reservation> reservations = reservationRepository.getAllReservation();

        assertThat(reservations).containsExactly(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationRepository.deleteReservation(1);
        List<Reservation> reservations = reservationRepository.getAllReservation();

        assertThat(reservations).isNotIn(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        reservationRepository.addReservation(new ReservationCommand("테스트", "2023-08-15", 1, 1), new ReservationTime(1, "15:14"), new ReservationTheme(1, "theme", "description", "imageUrl"));

        List<Reservation> reservations = reservationRepository.getAllReservation();

        Reservation expectedReservation = new Reservation(2, "테스트", "2023-08-15", new ReservationTime(1, "10:00"), new ReservationTheme(1, "테마1", "테마 설명", "image url"));

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations).contains(expectedReservation);
    }

    @Test
    @DisplayName("이름 필터링 된 전체 예약 가져오는 지 테스트")
    void getReservationByNameTest() {
        insertReservation("테스트", "2023-09-15", 1, 1);
        insertReservation("테스트2", "2023-10-15", 1, 1);
        insertReservation("테스트", "2023-11-15", 1, 1);
        insertReservation("테스트2", "2023-12-15", 1, 1);


        List<Reservation> reservations = reservationRepository.getAllReservationByName("테스트");

        List<Reservation> expectedReservation = List.of(
                new Reservation(2, "테스트", "2023-09-15", new ReservationTime(1, "10:00"), new ReservationTheme(1, "테마1", "테마 설명", "image url")),
                new Reservation(4, "테스트", "2023-11-15", new ReservationTime(1, "10:00"), new ReservationTheme(1, "테마1", "테마 설명", "image url"))
        );

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations).containsAll(expectedReservation);
    }

    @Test
    @DisplayName("정상적으로 해당 예약 themeId 존재하는 지 테스트")
    void existsByTimeIdTest() {
        insertReservationTheme("테마2", "테마 설명", "image url");

        assertThat(reservationRepository.existsByThemeId(1)).isTrue();
        assertThat(reservationRepository.existsByThemeId(2)).isFalse();
    }

    @Test
    @DisplayName("정상적으로 해당 예약 timeId 존재하는 지 테스트")
    void existByThemeIdTest() {
        insertReservationTime("11:00");

        assertThat(reservationRepository.existsByTimeId(1)).isTrue();
        assertThat(reservationRepository.existsByTimeId(2)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 2023-08-05, true",
            "1, 2, 2023-08-05, false",
            "1, 1, 2023-08-04, false",
            "1, 2, 2023-08-04, false",
            "2, 1, 2023-08-04, false",
            "2, 2, 2023-08-04, false"
    })
    @DisplayName("정상적으로 해당 예약 themeId, 예약 timeId, 예약 날짜가 존재하는 지 테스트")
    void existsByTimeIdAndThemeIdAndDateTest(long timeId, long themeId, String date, boolean expect) {
        assertThat(reservationRepository.existsByTimeIdAndThemeIdAndDate(timeId, themeId, date)).isEqualTo(expect);
    }
}

package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationInfo;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;

public class ReservationRepositoryTest extends BaseRepositoryTest {
    private ReservationRepository reservationRepository;

    private final ReservationInfo INIT_RESERVATION = new ReservationInfo(
            1,
            "브라운",
            "2023-08-05",
            new ReservationTime(1, LocalTime.parse("10:00")),
            new Theme(1, "테마1", "테마 설명", "image url")
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
    @DisplayName("존재하는 ID로 조회 시 해당 예약을 반환한다")
    void getReservationTest() {
        Optional<Reservation> reservation = reservationRepository.getReservation(1);

        assertThat(reservation).isPresent();
        assertThat(reservation.get().name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 empty를 반환한다")
    void getReservationEmptyTest() {
        Optional<Reservation> reservation = reservationRepository.getReservation(999L);

        assertThat(reservation).isEmpty();
    }

    @Test
    @DisplayName("전체 예약 테스트 정상적으로 가져오는 지 테스트")
    void getAllReservationTest() {
        List<ReservationInfo> reservations = reservationRepository.getAllReservation(null);

        assertThat(reservations).containsExactly(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationRepository.deleteReservation(1);
        List<ReservationInfo> reservations = reservationRepository.getAllReservation(null);

        assertThat(reservations).isNotIn(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        LocalDate date = LocalDate.now();
        reservationRepository.addReservation(new ReservationCommand("테스트", date, 1, 1), new ReservationTime(1, LocalTime.parse("15:14")), new Theme(1, "theme", "description", "imageUrl"));

        List<ReservationInfo> reservations = reservationRepository.getAllReservation(null);

        ReservationInfo expectedReservation = new ReservationInfo(2, "테스트", date, new ReservationTime(1, LocalTime.parse("10:00")), new Theme(1, "테마1", "테마 설명", "image url"));

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


        List<ReservationInfo> reservations = reservationRepository.getAllReservation("테스트");

        List<ReservationInfo> expectedReservation = List.of(
                new ReservationInfo(2, "테스트", "2023-09-15", new ReservationTime(1, LocalTime.parse("10:00")), new Theme(1, "테마1", "테마 설명", "image url")),
                new ReservationInfo(4, "테스트", "2023-11-15", new ReservationTime(1, LocalTime.parse("10:00")), new Theme(1, "테마1", "테마 설명", "image url"))
        );

        assertThat(reservations.size()).isEqualTo(2);
        assertThat(reservations).containsAll(expectedReservation);
    }

    @Test
    @DisplayName("빈 문자열이나 공백이 들어오면 전체를 조회한다")
    void getAllReservationWithBlankNameTest() {
        insertReservation("테스트", "2023-09-15", 1, 1);

        List<ReservationInfo> resultWithEmpty = reservationRepository.getAllReservation("");
        List<ReservationInfo> resultWithBlank = reservationRepository.getAllReservation("   ");

        assertThat(resultWithEmpty).hasSize(2);
        assertThat(resultWithBlank).hasSize(2);
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
        assertThat(reservationRepository.existsByTimeIdAndThemeIdAndDate(timeId, themeId, LocalDate.parse(date))).isEqualTo(expect);
    }

    @Test
    @DisplayName("기존 예약의 정보를 수정 테스트")
    void updateReservationTest() {
        LocalDate date = LocalDate.now();
        insertReservationTime("12:00");
        insertReservationTheme("테마2", "설명2", "url2");
        ReservationCommand updateCommand = new ReservationCommand("테스트", date, 2L, 2L);

        int updatedCount = reservationRepository.updateAll(1L, updateCommand);

        assertThat(updatedCount).isEqualTo(1);

        Reservation updatedInfo = reservationRepository.getReservation(1L).get();
        assertAll(
                () -> assertThat(updatedInfo.name()).isEqualTo("테스트"),
                () -> assertThat(updatedInfo.date()).isEqualTo(date),
                () -> assertThat(updatedInfo.timeId()).isEqualTo(2L),
                () -> assertThat(updatedInfo.themeId()).isEqualTo(2L)
        );
    }
}

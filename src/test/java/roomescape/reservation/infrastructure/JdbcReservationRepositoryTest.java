package roomescape.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;
import roomescape.reservationTime.infrastructure.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infrastructure.JdbcThemeRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private DataSource dataSource;
    private ReservationRepository reservationRepository;
    private Long themeId;
    private Long timeId;

    @BeforeEach
    void beforeEach() {
        reservationRepository = new JdbcReservationRepository(dataSource);
        ThemeRepository themeRepository = new JdbcThemeRepository(dataSource);
        ReservationTimeRepository timeRepository = new JdbcReservationTimeRepository(dataSource);

        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 10));
        timeId = timeRepository.save(reservationTime);
        Theme theme = Theme.createWithoutId("a", "a", "a");
        themeId = themeRepository.save(theme);
    }

    @Test
    @DisplayName("저장 후 아이디 반환 테스트")
    void save_test() {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation = Reservation.createWithoutId(
                LocalDateTime.of(1999, 11, 2, 20, 10), "a", LocalDate.of(2000, 11, 2), reservationTime, theme);

        Long save = reservationRepository.save(reservation);

        assertThat(save).isNotNull();
    }

    @Test
    @DisplayName("날짜와 테마 관련 조회 테스트")
    void find_by_themeId_and_date() {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation1 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        Reservation reservation2 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "b",
                LocalDate.of(2000, 10, 2), reservationTime, theme);
        Reservation reservation3 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "c",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);

        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(LocalDate.of(2000, 11, 2), themeId);
        List<String> names = reservations.stream()
                .map(Reservation::getName)
                .toList();

        assertAll(
                () -> assertThat(reservations).hasSize(2),
                () -> assertThat(names).contains("a", "c")
        );
    }

    @ParameterizedTest
    @DisplayName("삭제 성공 관련 테스트")
    @CsvSource({"0,true", "1,false"})
    void delete_test(Long plus, boolean expected) {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation);

        boolean isDeleted = reservationRepository.deleteBy(themeId + plus);

        assertThat(isDeleted).isEqualTo(expected);
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void find_all_test() {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation1 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        Reservation reservation2 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "b",
                LocalDate.of(2000, 10, 2), reservationTime, theme);
        Reservation reservation3 = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "c",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);

        List<Reservation> reservations = reservationRepository.findAll();
        List<String> names = reservations.stream()
                .map(Reservation::getName)
                .toList();

        assertAll(
                () -> assertThat(reservations).hasSize(3),
                () -> assertThat(names).contains("a", "b", "c")
        );
    }

    @ParameterizedTest
    @DisplayName("예약 시간 유무 조회 테스트")
    @CsvSource({"0,true", "1,false"})
    void exist_by_time(Long plus, boolean expected) {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation);

        boolean existed = reservationRepository.existByReservationTimeId(themeId + plus);

        assertThat(existed).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 조건 조회 테스트")
    void exist_by_test() {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(timeId, "a", "a", "a");
        Reservation reservation = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation);

        assertAll(
                () -> assertThat(reservationRepository.hasSameReservation(
                        createReservationForTestBy(themeId, LocalDate.of(2000, 11, 3),
                                LocalTime.of(10, 10)))).isFalse(),
                () -> assertThat(reservationRepository.hasSameReservation(
                        createReservationForTestBy(themeId, LocalDate.of(2000, 11, 2),
                                LocalTime.of(10, 11)))).isFalse(),
                () -> assertThat(reservationRepository.hasSameReservation(
                        createReservationForTestBy(themeId + 1, LocalDate.of(2000, 11, 2),
                                LocalTime.of(10, 10)))).isFalse(),
                () -> assertThat(reservationRepository.hasSameReservation(
                        createReservationForTestBy(themeId, LocalDate.of(2000, 11, 2),
                                LocalTime.of(10, 10)))).isTrue()
        );
    }

    private Reservation createReservationForTestBy(Long themeId, LocalDate localDate, LocalTime localTime) {
        ReservationTime reservationTime = ReservationTime.createWithId(1L, localTime);
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        return Reservation.createWithId(1L, "a", localDate, reservationTime, theme);
    }


    @ParameterizedTest
    @DisplayName("테마 유무 조회 테스트")
    @CsvSource({"0,true", "1,false"})
    void exist_by_theme(Long plus, boolean expected) {
        ReservationTime reservationTime = ReservationTime.createWithId(timeId, LocalTime.of(10, 10));
        Theme theme = Theme.createWithId(themeId, "a", "a", "a");
        Reservation reservation = Reservation.createWithoutId(LocalDateTime.of(1999, 11, 2, 20, 10), "a",
                LocalDate.of(2000, 11, 2), reservationTime, theme);
        reservationRepository.save(reservation);

        boolean existed = reservationRepository.existByThemeId(themeId + plus);

        assertThat(existed).isEqualTo(expected);
    }
}

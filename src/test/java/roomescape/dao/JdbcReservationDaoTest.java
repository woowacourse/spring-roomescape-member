package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
class JdbcReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcReservationDao jdbcReservationDao;

    @BeforeEach
    void setUp() {
        jdbcReservationDao = new JdbcReservationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 목록을 조회한다")
    void returnReservationList() {
        // when 
        List<Reservation> reservations = jdbcReservationDao.findAll();
        Reservation reservation = reservations.getFirst();

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservation)
                        .extracting("id", "name", "date")
                        .containsExactly(1L, "name", LocalDate.of(2025, 1, 1))
        );
    }

    @Test
    @DisplayName("예약을 저장한다")
    void saveReservation() {
        // given
        Theme theme = new Theme(1L, "name", "description", "thumbnail");
        ReservationTime time = new ReservationTime(1L, LocalTime.of(11, 0));
        Reservation reservation = new Reservation("name", LocalDate.of(2025, 1, 1), time, theme);

        // when
        Long savedId = jdbcReservationDao.save(reservation);
        Boolean result = existsReservationById(savedId);

        // then 
        assertThat(savedId).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation() {
        // given
        Boolean exists = existsReservationById(1L);

        // when
        jdbcReservationDao.deleteById(1L);
        Boolean notExists = existsReservationById(1L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "1, TRUE",
            "2, FALSE"
    })
    @DisplayName("예약 시간 ID로 예약 존재 여부를 판단한다")
    void existsReservationByTimeId(Long timeId, boolean expected) {
        // when
        Boolean result = jdbcReservationDao.existsByTimeId(timeId);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2025-01-01, 1, TRUE",
            "2025-01-01, 2, FALSE",
            "2025-01-02, 1, FALSE",
            "2025-01-02, 2, FALSE"
    })
    @DisplayName("예약 날짜, 예약 시간 ID로 예약 존재 여부를 판단한다")
    void existsReservationByDateAndTimeId(LocalDate date, Long timeId, boolean expected) {
        // when
        Boolean result = jdbcReservationDao.existsByDateAndTimeId(date, timeId);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2025-01-01, 1, 1, TRUE",
            "2025-01-01, 1, 2, FALSE",
            "2025-01-01, 2, 1, FALSE",
            "2025-01-02, 1, 1, FALSE",
            "2025-01-02, 1, 2, FALSE",
            "2025-01-02, 2, 2, FALSE"
    })
    @DisplayName("예약 날짜, 시간 ID, 테마 ID로 예약 존재 여부를 판단한다")
    void existsReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId, boolean expected) {
        // when
        Boolean result = jdbcReservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 범위 날짜의 10위까지의 인기 테마 ID를 조회해 반환한다")
    void findTop10ThemesByReservationCountBetweenDates() {
        // given
        LocalDate now = LocalDate.now();
        setupTestData(now);

        List<Long> expected = List.of(2L, 5L, 1L, 3L, 7L, 6L, 4L, 10L, 9L, 11L);

        // when
        List<Long> result = jdbcReservationDao.findTop10ByBetweenDates(now.minusDays(7), now);

        // then
        assertThat(result).hasSize(10);
        assertThat(result).containsExactlyElementsOf(expected);
    }

    private Boolean existsReservationById(Long id) {
        return jdbcTemplate.queryForObject(
                "select exists(select 1 from reservation where id = ?)",
                Boolean.class,
                id
        );
    }

    private void setupTestData(LocalDate now) {
        List<List<Object>> themeReservations = List.of(
                List.of(1L, 7, now.minusDays(3)),
                List.of(2L, 9, now.minusDays(5)),
                List.of(3L, 6, now.minusDays(2)),
                List.of(4L, 3, now.minusDays(4)),
                List.of(5L, 8, now.minusDays(1)),
                List.of(6L, 4, now.minusDays(2)),
                List.of(7L, 5, now.minusDays(3)),
                List.of(8L, 0, now.minusDays(1)),
                List.of(9L, 1, now.minusDays(4)),
                List.of(10L, 2, now.minusDays(5)),
                List.of(11L, 8, now.minusDays(8))
        );

        for (List<Object> reservation : themeReservations) {
            Long themeId = (Long) reservation.get(0);
            int count = (int) reservation.get(1);
            LocalDate date = (LocalDate) reservation.get(2);

            createReservations(themeId, count, date);
        }
    }

    private void createReservations(Long themeId, int count, LocalDate date) {
        for (int i = 0; i < count; i++) {
            insertReservation(date, themeId);
        }
    }

    private void insertReservation(LocalDate date, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "test_name", date, 1L, themeId
        );
    }
}

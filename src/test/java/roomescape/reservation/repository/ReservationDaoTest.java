package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import roomescape.common.exception.EntityNotFoundException;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.utils.JdbcTemplateUtils;

@ActiveProfiles("test")
@JdbcTest
@Import({ReservationDao.class})
class ReservationDaoTest {

    private final Long RESERVATION_TIME_ID = 1L;
    private final LocalTime RESERVATION_TIME_START_TIME = LocalTime.of(8, 0);
    private final Long THEME_ID = 1L;
    private final String THEME_NAME = "공포";
    private final String THEME_DESCRIPTION = "우테코 공포";
    private final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationDao reservationDao;

    @AfterEach
    void tearDown() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void test1() {
        // given
        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        LocalDateTime now = LocalDateTime.now();
        String name = "꾹";
        Reservation reservation = Reservation.withoutId(new Name(name), now.toLocalDate(), reservationTime, theme);

        // when
        Reservation result = reservationDao.save(reservation);

        // then
        assertThat(result.getName()).isEqualTo(new Name(name));
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTime().getId()).isEqualTo(RESERVATION_TIME_ID);
        assertThat(result.getTheme().getId()).isEqualTo(THEME_ID);
    }

    @DisplayName("id가 같다면 해당 예약 정보로 변경한다.")
    @Test
    void test4() {
        // given
        long reservationId = 1;
        LocalDateTime now = LocalDateTime.now();

        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        String originalName = "꾹";
        saveReservation(reservationId, originalName, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        String changedName = "드라고";
        Reservation updateReservation =
                new Reservation(reservationId, new Name(changedName), now.toLocalDate(), reservationTime, theme);

        // when
        Reservation result = reservationDao.save(updateReservation);

        // then
        assertThat(result).isEqualTo(updateReservation);
    }

    @DisplayName("존재하지 않는 id를 save한다면 예외를 반환한다.")
    @Test
    void test8() {
        // given
        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(1L, new Name("꾹"), now.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reservationDao.save(reservation))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("id로 예약 정보를 가져온다")
    @Test
    void test5() {
        // given
        LocalDateTime now = LocalDateTime.now();

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        long id = 1;
        String name = "꾹";
        saveReservation(id, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when
        Reservation result = reservationDao.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo(new Name(name));
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTimeId()).isEqualTo(RESERVATION_TIME_ID);
        assertThat(result.getReservationStartTime()).isEqualTo(now.toLocalTime());
        assertThat(result.getThemeId()).isEqualTo(THEME_ID);
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test6() {
        // given
        LocalDate date = LocalDate.now();

        saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        List<String> names = List.of("꾹", "헤일러", "라젤");

        String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";

        for (String name : names) {
            jdbcTemplate.update(sql, name, date, RESERVATION_TIME_ID, THEME_ID);
        }

        // when
        List<Reservation> result = reservationDao.findAll();

        // then
        List<String> resultNames = result.stream()
                .map(reservation -> reservation.getName().getValue())
                .toList();
        List<LocalDate> resultDates = result.stream()
                .map(Reservation::getReservationDate)
                .toList();
        List<LocalTime> resultTimes = result.stream()
                .map(Reservation::getReservationTime)
                .map(ReservationTime::getStartAt)
                .toList();
        List<String> themeNames = result.stream()
                .map(Reservation::getTheme)
                .map(Theme::getName)
                .toList();

        assertThat(resultNames).containsAll(names);

        for (LocalDate resultDate : resultDates) {
            assertThat(resultDate).isEqualTo(date);
        }

        for (LocalTime resultTime : resultTimes) {
            assertThat(resultTime).isEqualTo(RESERVATION_TIME_START_TIME);
        }

        for (String themeName : themeNames) {
            assertThat(themeName).isEqualTo(THEME_NAME);
        }
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test7() {
        // given
        Long reservationId = 2L;
        String name = "꾹";
        LocalDateTime now = LocalDateTime.now();

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        saveReservation(reservationId, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when
        reservationDao.deleteById(reservationId);

        // then
        String sql = "select count(*) from reservation where id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, reservationId);
        assertThat(count).isZero();
    }

    @DisplayName("날짜와 시간, 테마로 존재하는 예약인지 확인한다.")
    @CsvSource(value = {
            "29,29,true",
            "28,29,false"
    })
    @ParameterizedTest
    void test8(int day1, int day2, boolean expected) {
        // given
        Long reservationId = 2L;
        String name = "꾹";
        LocalDateTime now = LocalDateTime.of(2025, 4, day1, 10, 0);

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        saveReservation(reservationId, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when & then
        assertThat(
                reservationDao.existsByDateAndTimeIdAndThemeId(
                        LocalDate.of(2025, 4, day2), RESERVATION_TIME_ID, THEME_ID
                )
        ).isEqualTo(expected);
    }

    private ReservationTime saveReservationTime(Long id, LocalTime time) {
        String insertTimeSql = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(insertTimeSql, id, time);

        return new ReservationTime(id, time);
    }

    private Theme saveTheme(Long id, String name, String description, String thumbnail) {
        String insertTimeSql = "insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)";
        jdbcTemplate.update(insertTimeSql, id, name, description, thumbnail);

        return new Theme(id, name, description, thumbnail);
    }

    private void saveReservation(Long id, String name, LocalDate date, Long timeId, Long themeId) {
        String sql = "insert into reservation (id, name, date, time_id, theme_id) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, name, date, timeId, themeId);
    }
}

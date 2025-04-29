package roomescape.reservation.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.config.TestConfig;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.impl.ReservationDAO;
import roomescape.utils.JdbcTemplateUtils;

class ReservationDAOTest {

    private static final Long RESERVATION_TIME_ID = 1L;
    private static final LocalTime RESERVATION_TIME_START_TIME = LocalTime.of(8, 0);
    private static final Long THEME_ID = 1L;
    private static final String THEME_NAME = "공포";
    private static final String THEME_DESCRIPTION = "우테코 공포";
    private static final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    private JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        jdbcTemplate = TestConfig.getJdbcTemplate();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = TestConfig.getNamedParameterJdbcTemplate();
        reservationRepository = new ReservationDAO(namedParameterJdbcTemplate, TestConfig.getDataSource());
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void test1() {
        // given
        ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        LocalDateTime now = LocalDateTime.now();
        String name = "꾹";
        Reservation reservation = Reservation.withoutId(name, now.toLocalDate(), reservationTime, theme);

        // when
        Reservation result = reservationRepository.save(reservation);

        // then
        assertThat(result.getName()).isEqualTo(name);
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
                new Reservation(reservationId, changedName, now.toLocalDate(), reservationTime, theme);

        // when
        Reservation result = reservationRepository.save(updateReservation);

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
        Reservation reservation = new Reservation(1L, "꾹", now.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(reservation))
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
        Reservation result = reservationRepository.findById(id).get();

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTimeId()).isEqualTo(RESERVATION_TIME_ID);
        assertThat(result.getReservationStratTime()).isEqualTo(now.toLocalTime());
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
        List<Reservation> result = reservationRepository.findAll();

        // then
        List<String> resultNames = result.stream()
                .map(Reservation::getName)
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

        for(String themeName : themeNames) {
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
        reservationRepository.deleteById(reservationId);

        // then
        String sql = "select count(*) from reservation where id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, reservationId);
        assertThat(count).isZero();
    }

    @DisplayName("날짜와 시간으로 존재하는 예약인지 확인한다.")
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
        assertThat(reservationRepository.existsByDateAndTimeId(LocalDate.of(2025, 4, day2), RESERVATION_TIME_ID))
                .isEqualTo(expected);
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

    @AfterEach
    void cleanUp() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }
}

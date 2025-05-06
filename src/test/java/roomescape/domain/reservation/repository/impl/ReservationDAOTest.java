package roomescape.domain.reservation.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

/**
 * DAO 테스트 비활성화
 * 단순한 SQL 쿼리에 대하여 검증하는 구간이라고 느끼게 되서 DAO 대한 테스트 비활성화
 * 대신, Fake를 활용한 서비스 테스트와 서비스 + 실제 레포지토리를 함친 테스트를 하여 테스트 문제점을 파악할 수 있다고 보았음
 */

@JdbcTest
@Import(ReservationDAO.class)
class ReservationDAOTest {

    private static final Long RESERVATION_TIME_ID = 1L;
    private static final LocalTime RESERVATION_TIME_START_TIME = LocalTime.of(8, 0);
    private static final Long THEME_ID = 1L;
    private static final String THEME_NAME = "공포";
    private static final String THEME_DESCRIPTION = "우테코 공포";
    private static final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void test1() {
        // given
        final ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        final Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        final LocalDateTime now = LocalDateTime.now();
        final Name name = new Name("꾹");
        final Reservation reservation = Reservation.withoutId(name, now.toLocalDate(), reservationTime, theme);

        // when
        final Reservation result = reservationRepository.save(reservation);

        // then
        assertThat(result.getReservationName()).isEqualTo(name);
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTime()
                .getId()).isEqualTo(RESERVATION_TIME_ID);
        assertThat(result.getTheme()
                .getId()).isEqualTo(THEME_ID);
    }

    private ReservationTime saveReservationTime(final Long id, final LocalTime time) {
        final String insertTimeSql = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(insertTimeSql, id, time);

        return new ReservationTime(id, time);
    }

    private Theme saveTheme(final Long id, final String name, final String description, final String thumbnail) {
        final String insertTimeSql = "insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)";
        jdbcTemplate.update(insertTimeSql, id, name, description, thumbnail);

        return new Theme(id, name, description, thumbnail);
    }

    @DisplayName("id가 같다면 해당 예약 정보로 변경한다.")
    @Test
    void test4() {
        // given
        final long reservationId = 1;
        final LocalDateTime now = LocalDateTime.now();

        final ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        final Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        final String originalName = "꾹";
        saveReservation(reservationId, originalName, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        final Name changedName = new Name("드라고");
        final Reservation updateReservation = new Reservation(reservationId, changedName, now.toLocalDate(),
                reservationTime, theme);

        // when
        final Reservation result = reservationRepository.save(updateReservation);

        // then
        assertThat(result).isEqualTo(updateReservation);
    }

    private void saveReservation(final Long id, final String name, final LocalDate date, final Long timeId,
                                 final Long themeId) {
        final String sql = "insert into reservation (id, name, date, time_id, theme_id) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, name, date, timeId, themeId);
    }

    @DisplayName("존재하지 않는 id를 save한다면 예외를 반환한다.")
    @Test
    void test5() {
        // given
        final ReservationTime reservationTime = saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        final Theme theme = saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        final LocalDateTime now = LocalDateTime.now();
        final Name name = new Name("꾹");
        final Reservation reservation = new Reservation(1L, name, now.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reservationRepository.save(reservation)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("id로 예약 정보를 가져온다")
    @Test
    void test6() {
        // given
        final LocalDateTime now = LocalDateTime.now();

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        final long id = 1;
        final String name = "꾹";
        saveReservation(id, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when
        final Reservation result = reservationRepository.findById(id)
                .get();

        // then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getReservationDate()).isEqualTo(now.toLocalDate());
        assertThat(result.getReservationTimeId()).isEqualTo(RESERVATION_TIME_ID);
        assertThat(result.getReservationStartTime()).isEqualTo(now.toLocalTime());
        assertThat(result.getThemeId()).isEqualTo(THEME_ID);
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test7() {
        // given
        final LocalDate date = LocalDate.now();

        saveReservationTime(RESERVATION_TIME_ID, RESERVATION_TIME_START_TIME);
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        final List<String> names = List.of("꾹", "헤일러", "라젤");

        final String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";

        for (final String name : names) {
            jdbcTemplate.update(sql, name, date, RESERVATION_TIME_ID, THEME_ID);
        }

        // when
        final List<Reservation> result = reservationRepository.findAll();

        // then
        final List<String> resultNames = result.stream()
                .map(Reservation::getName)
                .toList();
        final List<LocalDate> resultDates = result.stream()
                .map(Reservation::getReservationDate)
                .toList();
        final List<LocalTime> resultTimes = result.stream()
                .map(Reservation::getReservationTime)
                .map(ReservationTime::getStartAt)
                .toList();
        final List<String> themeNames = result.stream()
                .map(Reservation::getTheme)
                .map(Theme::getName)
                .toList();

        assertThat(resultNames).containsAll(names);

        for (final LocalDate resultDate : resultDates) {
            assertThat(resultDate).isEqualTo(date);
        }

        for (final LocalTime resultTime : resultTimes) {
            assertThat(resultTime).isEqualTo(RESERVATION_TIME_START_TIME);
        }

        for (final String themeName : themeNames) {
            assertThat(themeName).isEqualTo(THEME_NAME);
        }
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test8() {
        // given
        final Long reservationId = 2L;
        final String name = "꾹";
        final LocalDateTime now = LocalDateTime.now();

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        saveReservation(reservationId, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when
        reservationRepository.deleteById(reservationId);

        // then
        final String sql = "select count(*) from reservation where id = ?";
        final int count = jdbcTemplate.queryForObject(sql, Integer.class, reservationId);
        assertThat(count).isZero();
    }

    @DisplayName("날짜와 시간으로 존재하는 예약인지 확인한다.")
    @CsvSource(value = {
            "29,29,true", "28,29,false"
    })
    @ParameterizedTest
    void test9(final int day1, final int day2, final boolean expected) {
        // given
        final Long reservationId = 2L;
        final String name = "꾹";
        final LocalDateTime now = LocalDateTime.of(2025, 4, day1, 10, 0);

        saveReservationTime(RESERVATION_TIME_ID, now.toLocalTime());
        saveTheme(THEME_ID, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        saveReservation(reservationId, name, now.toLocalDate(), RESERVATION_TIME_ID, THEME_ID);

        // when & then
        assertThat(reservationRepository.existsByDateAndTimeId(LocalDate.of(2025, 4, day2),
                RESERVATION_TIME_ID)).isEqualTo(expected);
    }

}

package roomescape.dao.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.dao.reservationTime.JdbcReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
class JdbcThemeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;
    @Autowired
    private JdbcReservationDao jdbcReservationDao;
    @Autowired
    private JdbcThemeDao jdbcThemeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("데이터베이스에서 전체 테마를 조회한다.")
    @Test
    void findAll() {

        // given
        final Theme theme1 = new Theme("test1", "test1", "test1");
        final Theme theme2 = new Theme("test1", "test1", "test1");
        final Theme savedTheme1 = jdbcThemeDao.create(theme1);
        final Theme savedTheme2 = jdbcThemeDao.create(theme2);

        // when
        final List<Theme> themes = jdbcThemeDao.findAll();

        // then
        assertThat(themes.size()).isEqualTo(2);
    }

    @DisplayName("데이터베이스에 테마를 저장한다.")
    @Test
    void createTest() {

        // given
        final Theme theme = new Theme("test1", "test1", "test1");

        // when
        final Theme savedTheme = jdbcThemeDao.create(theme);

        // then
        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo("test1"),
                () -> assertThat(savedTheme.getDescription()).isEqualTo("test1"),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo("test1")
        );
    }

    @DisplayName("데이터베이스에 특정 테마가 예약에 사용되지 않는 경우 삭제한다.")
    @Test
    void deleteTest() {

        // given
        final Theme theme = new Theme("test1", "test1", "test1");
        final Theme savedTheme = jdbcThemeDao.create(theme);

        // when & then
        assertThat(jdbcThemeDao.deleteIfNoReservation(savedTheme.getId())).isTrue();
    }

    @DisplayName("데이터베이스에 특정 테마가 예약에 사용되는 경우 삭제하지 않는다.")
    @Test
    void deleteIfReservationExist() {

        // given
        final LocalTime time = LocalTime.of(10, 10);
        final LocalDate date = LocalDate.now().plusDays(1);
        final Theme theme = new Theme("test", "test", "test");
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Reservation reservation = Reservation.create("test", date, savedReservationTime, savedTheme);
        final Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when & then
        assertThat(jdbcThemeDao.deleteIfNoReservation(savedTheme.getId())).isFalse();
    }

    @DisplayName("id로 테마를 찾는다.")
    @Test
    void findByIdTest() {

        // given
        final Theme theme = new Theme("test1", "test1", "test1");
        final Theme savedTheme = jdbcThemeDao.create(theme);

        // when
        final Optional<Theme> optionalTheme = jdbcThemeDao.findById(savedTheme.getId());

        // then
        assertAll(
                () -> assertThat(optionalTheme.isPresent()).isTrue(),
                () -> assertThat(optionalTheme.get()).isEqualTo(savedTheme)
        );
    }

    @DisplayName("")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {

        // given
        final LocalTime time = LocalTime.of(10, 10);
        final LocalDate beforeOneDay = LocalDate.now().minusDays(1);
        final LocalDate beforeEightDay = LocalDate.now().minusDays(8);
        final Theme popularTheme = new Theme("test", "test", "test");
        final Theme notPopularTheme = new Theme("test2", "test", "test");
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));
        final Theme savedPopularTheme = jdbcThemeDao.create(popularTheme);
        final Theme savedNotPopularTheme = jdbcThemeDao.create(notPopularTheme);
        final String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('test', ?, ?, ?)";
        jdbcTemplate.update(sql, beforeOneDay.toString(), savedReservationTime.getId(), savedPopularTheme.getId());
        jdbcTemplate.update(sql, beforeEightDay.toString(), savedReservationTime.getId(), savedNotPopularTheme.getId());

        // when
        final List<Theme> themes = jdbcThemeDao.findPopularThemesInRecentSevenDays(
                LocalDate.now().minusDays(7), beforeOneDay);

        // then
        assertThat(themes.getFirst()).isEqualTo(savedPopularTheme);
    }
}

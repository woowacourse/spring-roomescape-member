package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

class ReservationDaoTest {
    private ReservationDao reservationDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(dataSource);
        reservationDao = new ReservationDao(dataSource);
    }

    @Test
    void 예약_저장() {
        // given
        ReservationTime reservationTime = jdbcTemplate.queryForObject(
                "SELECT id, start_at FROM RESERVATION_TIME WHERE id = 1",
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime()
                ));
        Theme theme = jdbcTemplate.queryForObject("SELECT id, name, description, thumbnail FROM THEME WHERE id = 1",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ));
        User user = jdbcTemplate.queryForObject("SELECT id, name, email, password, roles FROM users WHERE id = 1"
                , (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.toList(rs.getString("roles"))
                ));
        Reservation reservation = new Reservation(null, user, LocalDate.of(2025, 12, 16), reservationTime, theme);

        // when
        Reservation saved = reservationDao.save(reservation);

        // then
        assertThat(reservationDao.countTotalReservation(null, null, null, null)).isEqualTo(26);
    }

    @Test
    void 예약_삭제() {
        // when
        boolean isDeleted = reservationDao.deleteById(1L);

        // then
        assertThat(isDeleted).isTrue();
        assertThat(reservationDao.countTotalReservation(null, null, null, null)).isEqualTo(24);
    }

    @Test
    void 삭제할_아이디가_없는_경우() {
        // when
        boolean isDeleted = reservationDao.deleteById(26L);

        // then
        assertThat(isDeleted).isFalse();
        assertThat(reservationDao.countTotalReservation(null, null, null, null)).isEqualTo(25);
    }

    @Test
    void time_id로_존재_여부_반환() {
        // given
        Long existTimeId = 1L;
        Long nonExistTimeId = 999L;

        // when
        boolean exist = reservationDao.isExistByTimeId(existTimeId);
        boolean nonExist = reservationDao.isExistByTimeId(nonExistTimeId);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void theme_id와_time_id와_날짜로_존재여부_반환() {
        // given
        LocalDate existDate = LocalDate.now().minusDays(25);
        Long existTimeId = 1L;
        Long existThemeId = 1L;

        LocalDate nonExistDate = LocalDate.now().minusDays(25);
        Long nonExistTimeId = 99L;
        Long nonExistThemeId = 99L;

        // when
        boolean exist = reservationDao.isExistByThemeIdAndTimeIdAndDate(
                existThemeId,
                existTimeId,
                existDate
        );
        boolean nonExist = reservationDao.isExistByThemeIdAndTimeIdAndDate(
                nonExistThemeId,
                nonExistTimeId,
                nonExistDate
        );

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void theme_id로_존재여부_반환() {
        // given
        Long existThemeId = 1L;
        Long nonExistThemeId = 999L;

        // when
        boolean exist = reservationDao.isExistByThemeId(existThemeId);
        boolean nonExist = reservationDao.isExistByThemeId(nonExistThemeId);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void theme_id와_날짜에_해당하는_예약_리스트_반환() {
        // given
        LocalDate date = LocalDate.now().minusDays(25);
        Long themeId = 1L;

        // when
        List<Reservation> reservations = reservationDao.findByThemeIdAndDate(themeId, date);

        // then
        assertThat(reservations).hasSize(1);
    }

    @Test
    void 예약의_전체_개수를_카운팅한다() {
        // when
        int count = reservationDao.countTotalReservation(null, null, null, null);

        // then
        assertThat(count).isEqualTo(25);
    }

    @Test
    void 시작_인덱스와_마지막_인덱스를_설정해_예약을_불러온다() {
        // given
        int start = 3;
        int end = 10;

        // when
        List<Reservation> reservations = reservationDao.findReservationsWithPage(start, end, null, null, null, null);

        // then
        assertThat(reservations).hasSize(8);
        assertThat(reservations.getFirst().getId()).isEqualTo(23L);
        assertThat(reservations.getLast().getId()).isEqualTo(16L);
    }
}

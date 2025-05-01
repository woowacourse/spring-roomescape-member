package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

class ReservationDaoTest {
    private static ReservationDao reservationDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        reservationDao = new ReservationDao(jdbcTemplate);
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
        Reservation reservation = new Reservation(null, "이름", LocalDate.of(2025, 12, 16), reservationTime, theme);

        // when
        Reservation saved = reservationDao.save(reservation);

        // then
        List<Reservation> all = reservationDao.findAll();
        assertThat(all).hasSize(26);
        assertThat(all.getLast().getId()).isEqualTo(saved.getId());
        assertThat(all.getLast().getName()).isEqualTo(saved.getName());
        assertThat(all.getLast().getDate()).isEqualTo(saved.getDate());
        assertThat(all.getLast().getReservationTime().getId()).isEqualTo(saved.getReservationTime().getId());
    }

    @Test
    void 예약_삭제() {
        // when
        boolean isDeleted = reservationDao.deleteById(1L);

        // then
        List<Reservation> all = reservationDao.findAll();
        assertThat(isDeleted).isTrue();
        assertThat(all).hasSize(24);
    }

    @Test
    void 삭제할_아이디가_없는_경우() {
        // when
        boolean isDeleted = reservationDao.deleteById(26L);

        // then
        List<Reservation> all = reservationDao.findAll();
        assertThat(isDeleted).isFalse();
        assertThat(all).hasSize(25);
    }

    @Test
    void 모든_예약을_반환한다() {
        // when
        List<Reservation> all = reservationDao.findAll();

        // then
        assertThat(all).hasSize(25);
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
        LocalDate existDate = LocalDate.of(2023, 3, 1);
        Long existTimeId = 1L;
        Long existThemeId = 1L;

        LocalDate nonExistDate = LocalDate.of(2025, 3, 1);
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
        LocalDate date = LocalDate.of(2023, 3, 1);
        Long themeId = 1L;

        // when
        List<Reservation> reservations = reservationDao.findByThemeIdAndDate(themeId, date);

        // then
        assertThat(reservations).hasSize(1);
    }
}

package roomescape.reservation.repository.dao;


import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.repository.entity.ReservationEntity;

@Sql(scripts = {"/import_reservation-time.sql", "/import_theme.sql"})
@JdbcTest
class ReservationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationDao reservationDao;

    private static final RowMapper<ReservationEntity> reservationRowMapper = (rs, rowNum) ->
            new ReservationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getObject("date", LocalDate.class),
                    rs.getLong("time_id"),
                    rs.getLong("theme_id"),
                    rs.getBoolean("is_cancelled")
            );

    @BeforeEach
    void setup() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Test
    void 예약을_저장한다() {
        //given
        String name = "user";
        LocalDate date = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        //when
        Long savedId = reservationDao.insert(name, date, timeId, themeId);

        //then
        ReservationEntity reservationEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM reservation WHERE id = ? AND is_deleted = FALSE;",
                reservationRowMapper, savedId);

        ReservationEntity expected = new ReservationEntity(savedId, name, date, timeId, themeId, false);

        Assertions.assertThat(reservationEntity)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 기준_날짜_이후의_예약만_조회한다() {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        reservationDao.insert("userA", yesterday, 1L, 1L);
        reservationDao.insert("userB", today, 1L, 1L);
        reservationDao.insert("userC", tomorrow, 1L, 1L);

        //when
        List<LocalDate> findDates = reservationDao.findAllOnOrAfter(today).stream()
                .map(ReservationEntity::getDate)
                .toList();

        //then
        Assertions.assertThat(findDates).containsExactly(today, tomorrow);
    }
}

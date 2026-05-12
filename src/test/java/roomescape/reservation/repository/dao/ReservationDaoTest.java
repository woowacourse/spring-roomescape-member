package roomescape.reservation.repository.dao;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.repository.entity.ReservationEntity;

@Sql(scripts = {"/import_theme.sql", "/import_reservation-time.sql"})
@JdbcTest
class ReservationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationDao reservationDao;

    @BeforeEach
    void setup() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Test
    void 저장된_예약을_조회한다() {
        // given
        Long id = reservationDao.insert("userA", LocalDate.of(2026, 1, 1), 1L, 1L);

        // when
        List<Long> reservationIds = reservationDao.findAll().stream()
                .map(ReservationEntity::getId)
                .toList();

        // then
        Assertions.assertThat(reservationIds).containsExactly(id);
    }

    @Test
    void 예약을_DB에_저장한다() {
        // given
        String name = "userA";
        LocalDate date = LocalDate.of(2026, 1, 1);

        // when
        Long savedId = reservationDao.insert(name, date, 1L, 1L);
        String sql = "SELECT id FROM reservation";
        List<Long> findIdList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("id"));

        // then
        Assertions.assertThat(findIdList.getFirst())
                .isEqualTo(savedId);
    }
}

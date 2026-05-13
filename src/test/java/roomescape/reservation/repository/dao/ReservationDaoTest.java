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

@Sql(scripts = {"/import_reservation-time.sql", "/import_theme.sql"})
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

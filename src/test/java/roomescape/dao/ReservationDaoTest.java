package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(ReservationDao.class)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;


    @DisplayName("모든 예약을 조회한다.")
    @Test
    void findAllTest() {
        List<Reservation> reservations = reservationDao.findAll();

        assertThat(reservations.size()).isGreaterThan(0);
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void insertTest() {
        Long index = jdbcTemplate.queryForObject("SELECT count(*) FROM reservation", Long.class);
        Long id = reservationDao.insert("2024-01-02", 1L, 1L, 1L);

        assertThat(id).isEqualTo(index + 1);
    }

    @DisplayName("요청 파라미터의 값이 올바르지 않으면 예외를 발생한다.")
    @Test
    void wrongInsertTest() {
        assertThatThrownBy(() -> reservationDao.insert( "2024-01-01", -1L, 1L, 1L))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> reservationDao.insert(null, 1L, 1L, 1L))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> reservationDao.insert("2024-01-01", 1L, -1L, 1L))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> reservationDao.insert("2024-01-01", 1L, 1L, -1L))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("ID를 이용하여 예약을 삭제한다.")
    @Test
    void deleteByIdTest() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation(date, time_Id, theme_id, member_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, "2024-01-03");
            ps.setLong(2, 1L);
            ps.setLong(3, 1L);
            ps.setLong(4, 1L);
            return ps;
        }, keyHolder);

        int countBeforeDelete = reservationDao.findAll().size();

        Long key = keyHolder.getKey().longValue();
        reservationDao.deleteById(key);

        int countAfterDelete = reservationDao.findAll().size();

        assertThat(countAfterDelete).isEqualTo(countBeforeDelete - 1);
    }

    @DisplayName("특정 시간에 대한 모든 예약의 개수를 조회한다.")
    @Test
    void countByTimeIdTest() {
        int count = reservationDao.countByTimeId(1L);

        assertThat(count).isGreaterThan(0);
    }

    @DisplayName("특정 테마에 대한 모든 예약의 개수를 조회한다.")
    @Test
    void countByThemeIdTest() {
        int count = reservationDao.countByThemeId(1L);

        assertThat(count).isGreaterThan(0);
    }

    @DisplayName("특정 날짜, 시간, 테마에 대한 모든 예약의 개수를 조회한다.")
    @Test
    void countTest() {
        int count = reservationDao.count(LocalDate.now().minusDays(1).toString(), 1L, 1L);

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("조건에 맞는 예약을 반환한다.")
    @Test
    void findFilteredReservationsTest() {
        ZoneId kst = ZoneId.of("Asia/Seoul");

        String dateFrom = LocalDate.now(kst).minusWeeks(1).toString();
        String dateTo = LocalDate.now(kst).minusDays(1).toString();

        List<Reservation> reservations = reservationDao.findFilteredReservations(1L, 1L, dateFrom, dateTo);

        assertThat(reservations.size()).isEqualTo(2);
    }
}

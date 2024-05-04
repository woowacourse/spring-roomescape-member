package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.MIA_RESERVATION_TIME;

class ReservationTimeJdbcDaoTest extends RepositoryTest {
    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        // given
        ReservationTime reservationTime = new ReservationTime(MIA_RESERVATION_TIME);

        // when
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);

        // when
        assertThat(savedReservationTime.getId()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        // given
        String insertSql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        jdbcTemplate.update(insertSql, MIA_RESERVATION_TIME.toString());

        // when
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(reservationTimes.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("Id로 예약 시간을 조회한다.")
    void findById() {
        // given
        String insertSql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(LocalTime.parse(MIA_RESERVATION_TIME)));
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        // when
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);

        // then
        assertThat(reservationTime).isPresent();
    }

    @Test
    @DisplayName("Id에 해당하는 예약 시간이 없다면 빈 Optional을 반환한다.")
    void findByNotExistingId() {
        // given
        Long id = 1L;

        // when
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    @DisplayName("Id로 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        String insertSql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(LocalTime.parse(MIA_RESERVATION_TIME)));
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        // when
        reservationTimeDao.deleteById(id);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time where id = ?", Integer.class, id);
        assertThat(count).isEqualTo(0);
    }
}

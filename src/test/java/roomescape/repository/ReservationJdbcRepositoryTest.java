package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.*;

class ReservationJdbcRepositoryTest extends RepositoryTest implements ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        String insertTimeSql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        jdbcTemplate.update(insertTimeSql, Time.valueOf(LocalTime.parse(MIA_RESERVATION_TIME)));
    }

    @Override
    @Test
    @DisplayName("예약을 저장한다.")
    public void save() {
        // given
        Long timeId = 1L;
        Reservation reservation = MIA_RESERVATION(new ReservationTime(timeId, MIA_RESERVATION_TIME));

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getId()).isNotNull();
    }

    @Override
    @Test
    @DisplayName("동일시간대의 예약 목록을 조회한다.")
    public void findAllByDateAndTime() {
        // given
        Long timeId = 1L;
        String insertSql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?), (?, ?, ?)";
        jdbcTemplate.update(
                insertSql,
                USER_MIA, Date.valueOf(MIA_RESERVATION_DATE), timeId,
                USER_TOMMY, Date.valueOf(MIA_RESERVATION_DATE), timeId
        );

        // when
        List<Reservation> reservations = reservationRepository.findAllByDateAndTime(MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));

        // then
        assertThat(reservations).hasSize(2)
                .extracting(Reservation::getName)
                .containsExactly(USER_MIA, USER_TOMMY);
    }

    @Override
    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    public void findAll() {
        // given
        Long timeId = 1L;
        String insertSql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, USER_MIA, MIA_RESERVATION_DATE, timeId);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(reservations.size()).isEqualTo(count);
    }

    @Override
    @Test
    @DisplayName("Id로 예약을 조회한다.")
    public void findById() {
        // given
        long timeId = 1L;
        String insertSql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
            ps.setString(1, USER_MIA);
            ps.setDate(2, Date.valueOf(MIA_RESERVATION_DATE));
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        // when
        Optional<Reservation> reservation = reservationRepository.findById(id);

        // then
        assertThat(reservation).isPresent();
    }

    @Override
    @Test
    @DisplayName("Id에 해당하는 예약이 없다면 빈 Optional을 반환한다.")
    public void findByNotExistingId() {
        // given
        Long id = 1L;

        // when
        Optional<Reservation> actualReservation = reservationRepository.findById(id);

        // then
        assertThat(actualReservation).isEmpty();
    }

    @Override
    @Test
    @DisplayName("Id로 예약을 삭제한다.")
    public void deleteById() {
        // given
        long timeId = 1L;
        String insertSql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[]{"id"});
            ps.setString(1, USER_MIA);
            ps.setDate(2, Date.valueOf(MIA_RESERVATION_DATE));
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        // when
        reservationRepository.deleteById(id);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation where id = ?", Integer.class, id);
        assertThat(count).isEqualTo(0);
    }
}

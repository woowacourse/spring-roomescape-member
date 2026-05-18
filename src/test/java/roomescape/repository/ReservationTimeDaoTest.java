package roomescape.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.ReservationTime;
import roomescape.domain.TimeStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        if (rs.getString("status").equals(TimeStatus.DELETED.toString())) {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class),
                    TimeStatus.DELETED
            );
        }

        return ReservationTime.of(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    };

    @Test
    @DisplayName("시간 생성 테스트")
    void CreateReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        ReservationTime reservationTimeFromQuery = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        assertThat(saved.id()).isEqualTo(reservationTimeFromQuery.id());
    }

    @Test
    @DisplayName("시간 삭제 테스트")
    void DeleteReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        reservationTimeDao.deleteByTimeId(saved.id());
        ReservationTime deletedTime = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        TimeStatus expected = TimeStatus.DELETED;
        TimeStatus actual = deletedTime.status();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("AVAILABLE 상태의 시간 ID로 조회하면 값이 반환된다.")
    void findByTimeIdTest() {
        jdbcTemplate.update("INSERT INTO reservation_time VALUES (1, '09:00', 'AVAILABLE')");

        Optional<ReservationTime> result = reservationTimeDao.findByTimeId(1L);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 시간 ID로 조회하면 빈 Optional이 반환된다.")
    void findByTimeIdNotFoundTest() {
        Optional<ReservationTime> result = reservationTimeDao.findByTimeId(999L);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("DELETED 상태의 시간 ID로 조회하면 빈 Optional이 반환된다.")
    void findByTimeIdDeletedTest() {
        jdbcTemplate.update("INSERT INTO reservation_time VALUES (1, '09:00', 'DELETED')");

        Optional<ReservationTime> result = reservationTimeDao.findByTimeId(1L);

        assertThat(result.isEmpty()).isTrue();
    }
}

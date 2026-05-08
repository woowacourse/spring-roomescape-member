package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.reservation.entity.ReservationTime;

@JdbcTest
class ReservationTimeJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeJdbcRepository(jdbcTemplate);

        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다.")
    void findAllTest() {
        // when
        List<ReservationTime> times = reservationTimeRepository.findAll();

        // then
        assertThat(times).hasSize(6);
        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .contains(
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        LocalTime.of(14, 0),
                        LocalTime.of(15, 0)
                );
    }

    @Test
    @DisplayName("ID로 예약 시간을 조회한다.")
    void findByIdTest() {
        // given
        long timeId = 1L;

        // when
        Optional<ReservationTime> found = reservationTimeRepository.findById(timeId);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(timeId);
        assertThat(found.get().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void saveTest() {
        // given
        ReservationTime time = ReservationTime.create(LocalTime.of(20, 0));

        // when
        ReservationTime saved = reservationTimeRepository.save(time);

        // then
        assertThat(saved.getId()).isNotNull();

        ReservationTime found = findReservationTimeById(saved.getId());
        assertThat(found.getStartAt()).isEqualTo(LocalTime.of(20, 0));
    }

    @Test
    @DisplayName("ID로 예약 시간을 삭제한다.")
    void deleteByIdTest() {
        // given
        long generatedId = insertReservationTime(LocalTime.of(22, 0));

        // when
        reservationTimeRepository.deleteById(generatedId);

        // then
        List<ReservationTime> times = findReservationTimesById(generatedId);
        assertThat(times).isEmpty();
    }

    private long insertReservationTime(LocalTime startAt) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", startAt);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private ReservationTime findReservationTimeById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForObject(
                "SELECT * FROM reservation_time WHERE id = :id",
                parameters,
                reservationTimeRowMapper()
        );
    }

    private List<ReservationTime> findReservationTimesById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(
                "SELECT * FROM reservation_time WHERE id = :id",
                parameters,
                reservationTimeRowMapper()
        );
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNumber) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }
}

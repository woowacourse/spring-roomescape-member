package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(reservationTime)
        ).longValue();

        return ReservationTime.of(
                generatedKey,
                reservationTime.getStartAt()
        );
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> ReservationTime.of(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at"))
                )
        );
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";

        List<ReservationTime> results = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> ReservationTime.of(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at"))
                ),
                id
        );
        return results.stream()
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}

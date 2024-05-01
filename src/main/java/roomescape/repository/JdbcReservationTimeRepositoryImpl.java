package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@Repository
public class JdbcReservationTimeRepositoryImpl implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcReservationTimeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(
            Objects.requireNonNull(jdbcTemplate.getDataSource()))
            .withTableName("reservation_time")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource saveSource = new BeanPropertySqlParameterSource(reservationTime);
        long id = simpleJdbcInsert
            .executeAndReturnKey(saveSource)
            .longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public ReservationTime findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
        ), id);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
            ));
    }

    @Override
    public Long countByStartTime(LocalTime localTime) {
        String sql = "SELECT COUNT(id) FROM reservation_time WHERE start_at = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, localTime);
    }
}

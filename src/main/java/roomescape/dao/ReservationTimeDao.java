package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationTimeDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public ReservationTime save(ReservationTime time) {
        String sql = """
                INSERT INTO reservation_time(start_at) VALUES(:startAt)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource(Map.of("startAt", time.getStartAt())),
                keyHolder,
                new String[]{"id"});

        Long id = keyHolder.getKey().longValue();

        return new ReservationTime(id, time.getStartAt());
    }

    public List<ReservationTime> findAll() {
        return namedParameterJdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                (rs, rowNum) -> {
                    return new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                }
        );
    }

    public boolean deleteById(Long id) {
        String sql = """
                DELETE FROM reservation_time WHERE id = :id
                """;
        int deletedRow = namedParameterJdbcTemplate.update(
                sql,
                Map.of("id", id)
        );
        return deletedRow == 1;
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = """
                SELECT id, start_at FROM reservation_time WHERE id = :id
                """;
        try {
            ReservationTime reservationTime = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    Map.of("id", id),
                    (rs, rowNum) -> new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    )
            );
            return Optional.ofNullable(reservationTime);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isExistTime(LocalTime time) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM RESERVATION_TIME WHERE start_at = :startAt)
                """;
        return namedParameterJdbcTemplate.queryForObject(
                sql,
                Map.of("startAt", Time.valueOf(time)),
                Boolean.class
                );
    }
}

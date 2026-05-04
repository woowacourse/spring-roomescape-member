package roomescape.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> Reservation.of(
        resultSet.getLong("reservation_id"),
        resultSet.getString("name"),
        resultSet.getDate("date").toLocalDate(),
        ReservationTime.of(
            resultSet.getLong("time_id"),
            resultSet.getTime("start_at").toLocalTime()
        )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("name", reservation.getName())
            .addValue("date", reservation.getDate())
            .addValue("time_id", reservation.getTime().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Reservation.of(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public List<Reservation> findAll() {
        String query = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as start_at
            FROM reservation as r
            INNER JOIN reservation_time as t
              ON r.time_id = t.id
            """;
        return jdbcTemplate.query(query, rowMapper);
    }

    public void deleteById(Long id) {
        String query = "delete from reservation where id = ?";
        jdbcTemplate.update(query, id);
    }
}

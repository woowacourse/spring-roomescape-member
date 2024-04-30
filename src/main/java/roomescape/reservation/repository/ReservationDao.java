package roomescape.reservation.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, __) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String query = "SELECT r.id, r.name, r.date, t.id AS time_id, t.start_at "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.time_id = t.id";
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.time().id());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    private Reservation findById(long id) {
        String query = "SELECT r.id, r.name, r.date, t.id AS time_id, t.start_at "
                + "FROM RESERVATION AS r "
                + "INNER JOIN RESERVATION_TIME AS t "
                + "ON r.id = ?";
        return jdbcTemplate.queryForObject(query, reservationRowMapper, id);
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE ID = ?", id);
    }
}

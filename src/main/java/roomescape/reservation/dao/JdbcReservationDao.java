package roomescape.reservation.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private static final RowMapper<Reservation> RESERVATION_MAPPER = (resultSet, row) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservation);
        Number id = jdbcInsert.executeAndReturnKey(parameterSource);
        return new Reservation(id.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """       
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                """;

        return jdbcTemplate.query(sql, RESERVATION_MAPPER);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}

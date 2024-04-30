package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private Reservation mapRowReservation(final ResultSet rs, final int rowNum) throws SQLException {
        return Reservation.from(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                new ReservationTime(rs.getLong("time_id"), null)
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservation";

        return jdbcTemplate.query(sql, this::mapRowReservation);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";

        return jdbcTemplate.query(sql, this::mapRowReservation, id)
                .stream()
                .findAny();
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        String sql = "SELECT * FROM reservation WHERE time_id = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, timeId)
                .isEmpty();
    }

    @Override
    public Reservation save(final Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addValue("time_id", reservation.getTime().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    @Override
    public int deleteById(final Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }
}

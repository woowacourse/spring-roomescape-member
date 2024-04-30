package roomescape.reservation.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Reservation> reservationRowMapper =
            (resultSet, rowNum) -> new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            );

    @Override
    public Long save(final Reservation reservation) {
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("time_id", reservation.getReservationTime().getId());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id, r.name, r.date, t.id as time_id, t.start_at 
                from reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        String sql = """
                select r.id, r.name, r.date, t.id as time_id, t.start_at 
                from reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                where r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(final Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findAllByTimeId(final Long timeId) {
        String sql = """
                select r.id, r.name, r.date, t.id as time_id, t.start_at
                from reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                where r.time_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, timeId);
    }

    @Override
    public boolean existByReservation(final Reservation reservation) {
        String sql = """
                select count(*)
                from reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                where r.name = ? and r.date = ? and r.time_id = ?
                """; // TODO: 줄바꿈 고쳐라잉
        return jdbcTemplate.queryForObject(sql, Integer.class,
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime().getId()) != 0;
    }
}

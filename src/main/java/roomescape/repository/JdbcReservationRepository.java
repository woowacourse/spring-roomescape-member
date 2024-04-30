package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    LocalDate.parse(resultSet.getString("date")),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            LocalTime.parse(resultSet.getString("time_value"))
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT "
                + "r.id AS reservation_id,"
                + " r.name,"
                + " r.date,"
                + " t.id AS time_id,"
                + " t.start_at AS time_value "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Reservation findById(Long id) {
        String sql = "SELECT "
                + "r.id AS reservation_id,"
                + " r.name,"
                + " r.date,"
                + " t.id AS time_id,"
                + " t.start_at AS time_value "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id "
                + "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    @Override
    public boolean isTimeIdExist(Long id) {
        String sql = "select id from reservation where time_id=? limit 1";
        try {
            jdbcTemplate.queryForObject(sql, Long.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Reservation> findByDateAndTimeId(LocalDate date, Long timeId) {
        try {
            String sql = "SELECT "
                    + "r.id AS reservation_id,"
                    + " r.name,"
                    + " r.date,"
                    + " t.id AS time_id,"
                    + " t.start_at AS time_value "
                    + "FROM reservation AS r "
                    + "INNER JOIN reservation_time AS t "
                    + "ON r.time_id = t.id "
                    + "WHERE r.date = ? AND t.id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, date, timeId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }


}

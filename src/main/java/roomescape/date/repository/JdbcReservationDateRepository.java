package roomescape.date.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.date.domain.ReservationDate;

@Repository
public class JdbcReservationDateRepository implements ReservationDateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationDate> reservationDateRowMapper = (resultSet, rowMapper) ->
            ReservationDate.load(
                    resultSet.getLong("id"),
                    resultSet.getDate("date").toLocalDate()
            );

    public JdbcReservationDateRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation_date")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<ReservationDate> findById(Long id) {
        String sql = "SELECT * FROM reservation_date where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, reservationDateRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationDate> findAll() {
        String sql = "SELECT * FROM reservation_date";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), reservationDateRowMapper);
    }

    @Override
    public List<ReservationDate> findAllAfterToday() {
        String sql = "SELECT id, date FROM reservation_date WHERE date >= :today ORDER BY date ASC";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("today", LocalDate.now());

        return jdbcTemplate.query(sql, params, reservationDateRowMapper);
    }

    @Override
    public ReservationDate save(ReservationDate reservationDate) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservationDate.date());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationDate.load(id, reservationDate.date());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM reservation_date WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        int deletedCount = jdbcTemplate.update(sql, params);
        return deletedCount > 0;
    }
}

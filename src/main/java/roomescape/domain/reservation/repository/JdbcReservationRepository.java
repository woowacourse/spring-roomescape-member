package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final String TABLE_NAME = "reservation";
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("reservation_date").toLocalDate(),
            new ReservationTime(rs.getLong("time_id"), rs.getTime("start_at").toLocalTime()),
            new Theme(rs.getLong("theme_id"), rs.getString("theme.name"), rs.getString("description"),
                    rs.getString("thumbnail"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("reservation_date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String query = """
                SELECT * FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                JOIN theme AS th
                On r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByTimeId(long timeId) {
        String query = """
                SELECT * FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                JOIN theme AS th
                On r.theme_id = th.id
                WHERE r.time_id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, timeId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId) {
        String query = """
                SELECT r.id FROM reservation AS r
                WHERE EXISTS(
                SELECT 1 FROM reservation 
                WHERE r.reservation_date = ? AND r.time_id = ? AND r.theme_id = ?)              
                """;

        try {
            // todo: Optional.ofNullable로 감쌀지 생각.
            jdbcTemplate.queryForObject(query, Long.class, date, timeId, themeId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT * FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                JOIN theme AS th
                On r.theme_id = th.id
                """;
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}

package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("reservation_date").toLocalDate(),
            new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme.name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
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
    public boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId) {
        String query = """
                SELECT EXISTS(
                    SELECT 1 FROM reservation
                    WHERE reservation_date = ? AND time_id = ? AND theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId);
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String query = """
                SELECT EXISTS(
                    SELECT 1 FROM reservation
                    WHERE time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId);
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        String query = """
                SELECT EXISTS(
                    SELECT 1 FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(query, Boolean.class, themeId);
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT r.id, r.name, r.reservation_date, r.time_id, t.start_at, r.theme_id, th.name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                JOIN theme AS th
                On r.theme_id = th.id
                """;
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM reservation WHERE id = ?";
        int deletedRowCount = jdbcTemplate.update(query, id);
        return deletedRowCount > 0;
    }
}

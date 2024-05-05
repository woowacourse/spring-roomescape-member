package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
            resultSet.getString("reservation.name"),
            resultSet.getDate("reservation.date").toLocalDate(),
            new Time(
                    resultSet.getLong("reservation_time.id"),
                    resultSet.getTime("reservation_time.start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("reservation.theme_id"),
                    resultSet.getString("theme.name"),
                    resultSet.getString("theme.description"),
                    resultSet.getString("theme.thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT * FROM reservation r 
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Reservation insert(final Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public List<Reservation> findByTimeId(final Long timeId) {
        String sql = """
                SELECT * FROM reservation r 
                JOIN reservation_time rt ON r.time_id = rt.id 
                JOIN theme t ON r.theme_id = t.id 
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, timeId);
    }

    public List<Reservation> findByTimeIdAndDateAndThemeId(final Long timeId, final LocalDate date, final Long themeId) {
        String sql = """
                SELECT * FROM reservation r 
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                WHERE r.time_id = ? AND r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, timeId, date, themeId);
    }

    public int deleteById(final Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

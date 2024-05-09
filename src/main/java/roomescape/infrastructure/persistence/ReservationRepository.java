package roomescape.infrastructure.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().value())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public void removeById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean hasDuplicateReservation(Reservation reservation) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ?
                """;

        int duplicatedCount = jdbcTemplate.queryForObject(
                sql,
                Integer.class, reservation.getDate(),
                reservation.getTimeId(),
                reservation.getThemeId()
        );

        return duplicatedCount > 0;
    }

    public boolean hasByTimeId(Long id) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE time_id = ?
                """;

        int hasCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return hasCount > 0;
    }

    public boolean hasByThemeId(Long id) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE theme_id = ?
                """;

        int hasCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return hasCount > 0;
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id AS reservation_id, 
                    r.name, 
                    r.date, 
                    t.id AS time_id, 
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, reservationRowMapper());
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> {
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));
            return new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Name(resultSet.getString("name")),
                    LocalDate.parse(resultSet.getString("date")),
                    new ReservationTime(resultSet.getLong("time_id"), startAt),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    )
            );
        };
    }
}

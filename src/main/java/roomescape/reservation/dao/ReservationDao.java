package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;

@Repository
public class ReservationDao {
    private static final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("date")),
                new ReservationTime(rs.getLong("time_id"),
                        LocalTime.parse(rs.getString("start_at"), DateTimeFormatter.ofPattern("HH:mm"))),
                rs.getLong("theme_id")
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> selectAll() {
        String sql = """
                        select r.id as reservation_id,
                            r.name,
                            r.date,
                            t.id as time_id,
                            t.start_at as start_at,
                            r.theme_id as theme_id
                        from reservation r
                        inner join reservation_time t
                        on r.time_id = t.id
                    """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Reservation> selectByName(String name) {
        String sql = """
                        select r.id as reservation_id,
                            r.name,
                            r.date,
                            t.id as time_id,
                            t.start_at as start_at,
                            r.theme_id as theme_id
                        from reservation r
                        inner join reservation_time t
                        on r.time_id = t.id
                        WHERE r.name =  ?
                    """;
        return jdbcTemplate.query(sql, rowMapper, name);
    }

    public boolean isAvailable(Long themeId, LocalDate date, Long timeId) {
        String sql = """
                select
                    CASE
                        WHEN r.id IS NULL THEN true
                        ELSE false
                    END AS is_available
                FROM reservation_time t
                LEFT JOIN reservation r
                ON t.id = r.time_id
                AND r.theme_id = ?
                AND r.date = ?
                WHERE t.id = ?
                """;

        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId, date, timeId));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public Reservation insert(Reservation reservation) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getThemeId());

        Long id = (long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getThemeId());
    }

    public int delete(Long id, String name) {
        String sql = "delete from reservation where id = ? AND name = ?";
        return jdbcTemplate.update(sql, id, name);
    }
}

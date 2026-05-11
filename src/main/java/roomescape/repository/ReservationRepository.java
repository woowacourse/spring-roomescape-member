package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.global.exception.ReservationNotFoundException;

@Repository
public class ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        ReservationTime reservationTime = ReservationTime.from(
                rs.getLong("time_id"),
                rs.getObject("time_start_at", LocalTime.class)
        );
        Theme theme = Theme.from(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("image_url")
        );
        return Reservation.from(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                reservationTime,
                theme
        );
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll(int size, int offset) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.image_url
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                ORDER BY r.id
                LIMIT :size OFFSET :offset
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", offset);
        return jdbcTemplate.query(sql, parameters, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Reservation.from(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedCount = jdbcTemplate.update(sql, parameters);

        if (deletedCount == 0) {
            throw new ReservationNotFoundException();
        }
    }
}

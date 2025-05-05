package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
        return Reservation.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                reservationTime,
                theme
        );
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                """;

        return this.jdbcTemplate.query(sql,
                RESERVATION_ROW_MAPPER);
    }

    @Override
    public Reservation create(Reservation reservation) {
        Map<String, Object> parameter = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getReservationTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );
        Long newId = simpleJdbcInsert.executeAndReturnKey(parameter).longValue();
        return reservation.withId(newId);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = :id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, parameter);
    }

    @Override
    public List<Reservation> findByTimeId(Long timeId) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.time_id = :timeId
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("timeId", timeId);
        return jdbcTemplate.query(sql, parameter, RESERVATION_ROW_MAPPER);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.id = :id
                """;

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, parameter, RESERVATION_ROW_MAPPER);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.id as time_id,
                    rt.start_at,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description,
                    t.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.id
                JOIN theme AS t
                ON r.theme_id = t.id
                WHERE r.date = :date AND rt.start_at = :startAt
                """;
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("startAt", time);
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, parameter, RESERVATION_ROW_MAPPER);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

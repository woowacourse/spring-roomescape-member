package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

@Repository
public class H2ReservationDao implements ReservationDao {

    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("time_value", LocalTime.class)
        );
        Theme theme = new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_thumbnail")
        );
        return new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                reservationTime,
                theme);
    };


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public H2ReservationDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                     r.id AS id,
                     r.name AS name,
                     r.date AS date,
                     t.id AS time_id,
                     t.start_at AS time_value,
                     th.id AS theme_id,
                     th.name AS theme_name,
                     th.description AS theme_description,
                     th.thumbnail AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES(:name, :date, :time_id, :theme_id)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        jdbcTemplate.update(sql, mapSqlParameterSource, keyHolder);

        Number key = keyHolder.getKey();
        return new Reservation(key.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                     r.id AS id,
                     r.name AS name,
                     r.date AS date,
                     t.id AS time_id,
                     t.start_at AS time_value,
                     th.id AS theme_id,
                     th.name AS theme_name,
                     th.description AS theme_description,
                     th.thumbnail AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE r.id = :id
                """;
        List<Reservation> findReservation = jdbcTemplate.query(
                sql, new MapSqlParameterSource("id", id), ROW_MAPPER);

        return findReservation.stream().findFirst();
    }

    @Override
    public boolean isExistByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS 
                    (SELECT 1 
                     FROM reservation 
                     WHERE time_id = :time_id
                    )
                """;
        return Boolean.TRUE == jdbcTemplate.queryForObject(
                sql, new MapSqlParameterSource("time_id", timeId), Boolean.class);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                     r.id AS id,
                     r.name AS name,
                     r.date AS date,
                     t.id AS time_id,
                     t.start_at AS time_value,
                     th.id AS theme_id,
                     th.name AS theme_name,
                     th.description AS theme_description,
                     th.thumbnail AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE th.id = :theme_id AND r.date = :date
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("theme_id", themeId)
                .addValue("date", date);
        return jdbcTemplate.query(sql, mapSqlParameterSource, ROW_MAPPER);
    }
}

package roomescape.reservation.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("reservation_name"),
                    resultSet.getObject("reservation_date", LocalDate.class),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getObject("start_at", LocalTime.class)
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    ));

    @Override
    public Reservation save(Reservation reservation) {
        String insertReservationSql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (:name, :date, :timeId, :themeId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(insertReservationSql, params, keyHolder);

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """                
                SELECT                
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail_url AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt
                    ON r.time_id = rt.id
                INNER JOIN theme t
                    ON r.theme_id = t.id
                """;

        return template.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail_url AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt
                    ON r.time_id = rt.id
                INNER JOIN theme t
                    ON r.theme_id = t.id
                WHERE r.id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<Reservation> reservations = template.query(sql, params, reservationRowMapper);
        return reservations.stream().findFirst();
    }

    @Override
    public List<Reservation> findByName(String name) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail_url AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt
                    ON r.time_id = rt.id
                INNER JOIN theme t
                    ON r.theme_id = t.id
                WHERE r.name = :name
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name);

        return template.query(sql, params, reservationRowMapper);
    }

    @Override
    public void update(Reservation reservation) {
        String sql = """
                UPDATE reservation
                SET date = :date, time_id = :timeId
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", reservation.getId())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTimeId());

        template.update(sql, params);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return template.update(sql, params);
    }

    @Override
    public int deleteByName(String name) {
        String sql = "DELETE FROM reservation WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name);

        template.update(sql, params);
    }

    @Override
    public List<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId) {
        String sql = "SELECT time_id " +
                "FROM reservation " +
                "WHERE date = :date AND theme_id = :themeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return template.query(sql, params,
                (rs, rowNum) -> rs.getLong("time_id"));
    }
}

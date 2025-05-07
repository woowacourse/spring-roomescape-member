package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.ThemeName;
import roomescape.model.UserName;

@Repository
public class ReservationDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            var reservationTime = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()
            );

            var theme = new Theme(
                    rs.getLong("theme_id"),
                    new ThemeName(rs.getString("name")),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            );

            return new Reservation(
                    rs.getLong("reservation_id"),
                    new UserName(rs.getString("name")),
                    rs.getDate("date").toLocalDate(),
                    reservationTime,
                    theme
            );
        };
    }

    public Reservation save(Reservation reservation) {
        String sql = """
                INSERT INTO reservation(name, date, time_id, theme_id) 
                VALUES(:name, :date, :timeId, :themeId)
                """;

        Map<String, Object> params = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "timeId", reservation.getReservationTime().getId(),
                "themeId", reservation.getTheme().getId()
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"id"});

        Long id = keyHolder.getKey().longValue();

        return new Reservation(
                id,
                new UserName(reservation.getName()),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        int deletedRow = namedParameterJdbcTemplate.update(sql, Map.of("id", id));

        return deletedRow == 1;
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                """;
        return namedParameterJdbcTemplate.query(
                sql,
                reservationRowMapper()
        );
    }

    public boolean isExistByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = :timeId)
                """;

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                Map.of("timeId", timeId),
                Boolean.class
        );
    }

    public boolean isExistByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE theme_id = :themeId AND time_id = :timeId AND date = :date)
                """;
        Map<String, Object> params = Map.of(
                "themeId", themeId,
                "timeId", timeId,
                "date", date
        );

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                Boolean.class);
    }

    public boolean isExistByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE theme_id = :themeId)
                """;

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                Map.of("themeId", themeId),
                Boolean.class
        );
    }

    public List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                SELECT r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                WHERE theme_id = :themeId
                    AND
                    date = :date
                """;

        Map<String, Object> params = Map.of(
                "themeId", themeId,
                "date", date
        );

        return namedParameterJdbcTemplate.query(
                sql,
                params,
                reservationRowMapper()
        );
    }
}

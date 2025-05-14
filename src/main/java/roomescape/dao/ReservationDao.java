package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
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
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.model.ThemeName;
import roomescape.model.User;
import roomescape.model.UserName;

@Repository
public class ReservationDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            var user = new User(
                    rs.getLong("user_id"),
                    new UserName(rs.getString("user_name")),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role"))
            );

            var reservationTime = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()
            );

            var theme = new Theme(
                    rs.getLong("theme_id"),
                    new ThemeName(rs.getString("theme_name")),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            );

            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getDate("date").toLocalDate(),
                    user,
                    reservationTime,
                    theme
            );
        };
    }

    public Reservation save(Reservation reservation) {
        String sql = """
                INSERT INTO reservation(date, user_id, time_id, theme_id) 
                VALUES(:date, :userId, :timeId, :themeId)
                """;

        Map<String, Object> params = Map.of(
                "date", reservation.getDate(),
                "userId", reservation.getUser().getId(),
                "timeId", reservation.getReservationTime().getId(),
                "themeId", reservation.getTheme().getId()
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"id"});

        Long id = keyHolder.getKey().longValue();

        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getUser(),
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
                    r.date,
                    u.id as user_id,
                    u.name as user_name,
                    u.email,
                    u.password,
                    u.role,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN users as u 
                ON r.user_id = u.id
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
                    r.date,
                    u.id as user_id,
                    u.name as user_name,
                    u.email,
                    u.password,
                    u.role,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN users as u
                ON r.user_id = u.id
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

    public List<Reservation> findFilteredAll(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                SELECT r.id as reservation_id,
                    r.date,
                    u.id as user_id,
                    u.name as user_name,
                    u.email,
                    u.password,
                    u.role,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN users as u
                ON r.user_id = u.id
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                WHERE 1=1
                """;
        Map<String, Object> params = new HashMap<>();
        if (themeId != null) {
            sql += " AND theme_id = :themeId";
            params.put("themeId", themeId);

        }
        if (memberId != null) {
            sql += " AND user_id = :memberId";
            params.put("memberId", memberId);
        }
        if (dateFrom != null) {
            sql += " AND date >= :dateFrom";
            params.put("dateFrom", dateFrom);
        }
        if (dateTo != null) {
            sql += " AND date <= :dateTo";
            params.put("dateTo", dateTo);
        }
        return namedParameterJdbcTemplate.query(
                sql,
                params,
                reservationRowMapper()
        );
    }
}

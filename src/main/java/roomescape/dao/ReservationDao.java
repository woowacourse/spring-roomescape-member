package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.Role;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationDao {

    private static final String SELECT_RESERVATION = """
            SELECT 
                r.id, 
                r.date,
                rt.id AS time_id, 
                rt.start_at AS time_start_at,
                t.id AS theme_id, 
                t.name AS theme_name, 
                t.description AS theme_description, 
                t.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.email,
                m.name,
                m.password,
                m.role
            FROM reservation AS r
            INNER JOIN reservation_time AS rt ON r.time_id = rt.id
            INNER JOIN member AS m ON m.id = r.member_id
            INNER JOIN theme AS t ON t.id = r.theme_id 
            """;


    private final RowMapper<Reservation> actorRowMapper = (resultSet, rowNum) -> {
        Reservation reservation = new Reservation(
                resultSet.getLong("id"),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("email"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        Role.fromMemberRole(resultSet.getString("role"))
                ),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
        return reservation;
    };

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_RESERVATION, actorRowMapper);
    }

    public Optional<Reservation> findByDateAndTime(Reservation reservation) {
        String sql = SELECT_RESERVATION + " WHERE r.date = ? AND rt.id = ?";

        return jdbcTemplate.query(sql, actorRowMapper, reservation.getDate(), reservation.getTime().getId())
                .stream()
                .findFirst();
    }

    public Long saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getMember().getId());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByThemeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public boolean existsByReservationTimeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public List<Reservation> findByConditions(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        StringBuilder sql = new StringBuilder(SELECT_RESERVATION);
        List<Object> params = new ArrayList<>();

        Map<String, Object> conditions = new LinkedHashMap<>();
        if (themeId != null) {
            conditions.put("t.id = ?", themeId);
        }
        if (memberId != null) {
            conditions.put("m.id = ?", memberId);
        }
        if (dateFrom != null) {
            conditions.put("r.date >= ?", dateFrom);
        }
        if (dateTo != null) {
            conditions.put("r.date <= ?", dateTo);
        }

        boolean first = true;
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            if (first) {
                sql.append(" WHERE ");
                first = false;
            } else {
                sql.append(" AND ");
            }
            sql.append(entry.getKey());
            params.add(entry.getValue());
        }

        return jdbcTemplate.query(sql.toString(), actorRowMapper, params.toArray());
    }
}

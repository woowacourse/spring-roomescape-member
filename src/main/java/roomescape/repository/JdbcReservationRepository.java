package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        LocalDate date = rs.getObject("date", LocalDate.class);

        Member member = new Member(
                rs.getLong("member_id"),
                rs.getString("member_email"),
                rs.getString("member_password"),
                rs.getString("member_name"),
                Role.valueOf(rs.getString("member_role"))
        );

        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("time_start_at", LocalTime.class)
        );

        Theme theme = new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_thumbnail")
        );

        return new Reservation(id, date, member, time, theme);
    };

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                    SELECT
                        r.id,
                        r.date,
                        m.id AS member_id,
                        m.email AS member_email,
                        m.password AS member_password,
                        m.name AS member_name,
                        m.role AS member_role,
                        t.id AS time_id,
                        t.start_at AS time_start_at,
                        th.id AS theme_id,
                        th.name AS theme_name,
                        th.description AS theme_description,
                        th.thumbnail AS theme_thumbnail
                    FROM reservation AS r
                    JOIN member AS m
                    ON r.member_id = m.id
                    JOIN reservation_time AS t
                    ON r.time_id = t.id
                    JOIN theme AS th
                    ON r.theme_id = th.id
                """;

        List<Object> params = new ArrayList<>();
        boolean isFirstCondition = true;

        if (memberId != null) {
            sql += " WHERE m.id = ?";
            isFirstCondition = false;
            params.add(memberId);
        }

        if (themeId != null) {
            if (isFirstCondition) {
                sql += " WHERE th.id = ?";
                isFirstCondition = false;
            } else {
                sql += " AND th.id = ?";
            }
            params.add(themeId);
        }

        if (dateFrom != null) {
            if (isFirstCondition) {
                sql += " WHERE r.date >= ?";
                isFirstCondition = false;
            } else {
                sql += " AND r.date >= ?";
            }
            params.add(dateFrom);
        }

        if (dateTo != null) {
            if (isFirstCondition) {
                sql += " WHERE r.date <= ?";
            } else {
                sql += " AND r.date <= ?";
            }
            params.add(dateTo);
        }

        return jdbcTemplate.query(sql, rowMapper, params.toArray());
    }

    @Override
    public Reservation save(Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("member_id", reservation.getMemberId())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getMember(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByTimeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByThemeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByReservation(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}

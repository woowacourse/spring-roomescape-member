package roomescape.dao.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class)
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );

        Member member = new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                MemberRole.valueOf(resultSet.getString("member_role"))
        );

        return Reservation.load(
                resultSet.getLong("reservation_id"),
                resultSet.getObject("date", LocalDate.class),
                time,
                theme,
                member
        );
    };

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail,
                    me.id as member_id,
                    me.name as member_name,
                    me.email as member_email,
                    me.role as member_role
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member me
                    ON r.member_id = me.id
                """;
        return jdbcTemplate.query(sql, reservationMapper);
    }

    @Override
    public Reservation create(final Reservation reservation) {
        final SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>(Map.of(
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId(),
                "member_id", reservation.getMember().getId()));
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return Reservation.load(key.longValue(), reservation.getDate(), reservation.getTime(), reservation.getTheme(),
                reservation.getMember());
    }

    @Override
    public void delete(final long id) {
        final String sql = """
                DELETE
                FROM reservation
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findByThemeAndDateAndTime(final Reservation reservation) {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail,
                    me.id as member_id,
                    me.name as member_name,
                    me.email as member_email,
                    me.role as member_role
                FROM reservation as r 
                inner join reservation_time as t
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                inner join member as me
                on r.member_id = me.id
                WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationMapper, reservation.getDate(),
                reservation.getTime().getId(), reservation.getTheme().getId()).stream().findFirst();
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE id = ?""";
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<Reservation> findByThemeAndMemberAndDate(
            final Long themeId,
            final Long memberId,
            final LocalDate dateFrom,
            final LocalDate dateTo) {

        final StringBuilder sql = new StringBuilder();
        sql.append("""
                SELECT
                    r.id as reservation_id,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail,
                    me.id as member_id,
                    me.name as member_name,
                    me.email as member_email,
                    me.role as member_role
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member me
                    ON r.member_id = me.id
                WHERE 1=1
                """);

        // 파라미터를 담을 리스트
        final List<Object> params = new ArrayList<>();

        if (themeId != null) {
            sql.append(" AND r.theme_id = ?");
            params.add(themeId);
        }

        if (memberId != null) {
            sql.append(" AND r.member_id = ?");
            params.add(memberId);
        }

        if (dateFrom != null && dateTo != null) {
            sql.append(" AND r.date BETWEEN ? AND ?");
            params.add(dateFrom);
            params.add(dateTo);
        } else if (dateFrom != null) {
            sql.append(" AND r.date >= ?");
            params.add(dateFrom);
        } else if (dateTo != null) {
            sql.append(" AND r.date <= ?");
            params.add(dateTo);
        }
        sql.append(" ORDER BY r.date, t.start_at");
        return jdbcTemplate.query(sql.toString(), reservationMapper, params.toArray());
    }
}

package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservation;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate template;

    public ReservationRepositoryImpl(final JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        String sql = joinSql("WHERE rv.id = ?");
        final List<Reservation> reservations = template.query(sql, reservationV2RowMapper(), id);
        if (reservations.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(reservations.getFirst());
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final long timeId, final long themeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(template.queryForObject(sql, Boolean.class, date.toString(), timeId, themeId));
    }

    @Override
    public List<Reservation> findAllReservationsV2() {
        String sql = joinSql("");
        return template.query(sql, reservationV2RowMapper());
    }

    @Override
    public Reservation saveWithMember(final Reservation reservation) {
        String sql = "insert into reservation (member_id,date, time_id, theme_id) values (? ,?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getMemberId());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return reservation.toEntity(id);
    }

    @Override
    public int deleteById(final long id) {
        String sql = "delete from reservation where id = ?";
        return template.update(sql, id);
    }

    @Override
    public List<Reservation> findByMemberIdAndThemeIdAndDateFromAndDateTo(final long memberId, final long themeId,
                                                                          final LocalDate dateFrom,
                                                                          final LocalDate dateTo) {
        StringBuilder queryBuilder = new StringBuilder(joinSql(""));
        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (memberId > 0) {
            conditions.add("rv.member_id = ?");
            params.add(memberId);
        }

        if (themeId > 0) {
            conditions.add("rv.theme_id = ?");
            params.add(themeId);
        }

        if (dateFrom != null && dateTo != null) {
            conditions.add("rv.date BETWEEN ? AND ?");
            params.add(dateFrom.toString());
            params.add(dateTo.toString());
        } else if (dateFrom != null) {
            conditions.add("rv.date >= ?");
            params.add(dateFrom.toString());
        } else if (dateTo != null) {
            conditions.add("rv.date <= ?");
            params.add(dateTo.toString());
        }

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(String.join(" AND ", conditions));
        }

        return template.query(queryBuilder.toString(), reservationV2RowMapper(), params.toArray());
    }

    private RowMapper<Reservation> reservationV2RowMapper() {
        return (rs, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()
            );

            ReservationTheme reservationTheme = new ReservationTheme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_description"),
                    rs.getString("theme_thumbnail")
            );

            Member member = new Member(
                    rs.getLong("member_id"),
                    MemberRole.fromName(rs.getString("member_role")),
                    rs.getString("member_email"),
                    rs.getString("member_password"),
                    rs.getString("member_name"),
                    rs.getString("member_session_id")
            );

            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("date"),
                    reservationTime,
                    reservationTheme,
                    member
            );
        };
    }

    private String wrapExistsQuery(String sql) {
        return "SELECT EXISTS(" + sql + ")";
    }

    private String joinSql(String where) {
        String sql = """
                SELECT rv.id AS reservation_id, m.id AS member_id, m.role AS member_role, m.email AS member_email, m.name AS member_name, m.password AS member_password, m.session_id AS member_session_id, rv.date, t.id AS time_id, t.start_at AS time_value, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.id AS theme_id
                FROM reservation as rv
                INNER JOIN reservation_time AS t
                ON rv.time_id = t.id
                INNER JOIN reservation_theme AS th
                ON rv.theme_id = th.id
                INNER JOIN member AS m
                ON rv.member_id = m.id
                """;
        return sql + where;
    }
}

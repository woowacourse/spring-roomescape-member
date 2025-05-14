package roomescape.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.repository.criteria.ReservationCriteria;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            new Member(resultSet.getLong("member_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    null,
                    MemberRoleType.from(resultSet.getString("role"))),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()),
            new RoomTheme(resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("reservation_id");
    }

    @Override
    public long insert(final Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existSameReservation(final LocalDate date, final long timeId, final long themeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId));
    }

    @Override
    public List<Reservation> findAll() {
        final String query = """
                SELECT
                    reservation_id,
                    m.member_id, name, email, role,
                    date,
                    rt.time_id, start_at AS time_value,
                    t.theme_id, theme_name, description, thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.time_id
                JOIN theme AS t
                ON r.theme_id = t.theme_id
                JOIN member AS m
                ON r.member_id = m.member_id
                """;
        return jdbcTemplate.query(query, RESERVATION_ROW_MAPPER);
    }

    @Override
    public List<Reservation> findAllByCriteria(final ReservationCriteria criteria) {
        String sql = """
                SELECT
                    reservation_id,
                    m.member_id, name, email, role,
                    r.date,
                    rt.time_id, start_at AS time_value,
                    t.theme_id, theme_name, description, thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.time_id
                JOIN theme AS t
                ON r.theme_id = t.theme_id
                JOIN member AS m
                ON r.member_id = m.member_id
                WHERE 1 = 1
                """;
        StringBuilder query = new StringBuilder(sql);
        List<Object> params = new ArrayList<>();
        if (criteria.memberId() != null) {
            query.append(" AND m.member_id = ?");
            params.add(criteria.memberId());
        }
        if (criteria.themeId() != null) {
            query.append(" AND t.theme_id = ?");
            params.add(criteria.themeId());
        }
        if (criteria.from() != null) {
            query.append(" AND r.date >= ?");
            params.add(criteria.from());
        }
        if (criteria.to() != null) {
            query.append(" AND r.date <= ?");
            params.add(criteria.to());
        }
        System.out.println(criteria);
        System.out.println(query);
        return jdbcTemplate.query(query.toString(), RESERVATION_ROW_MAPPER, params.toArray());
    }

    @Override
    public Optional<Reservation> findById(long id) {
        final String query = """
                SELECT
                    reservation_id,
                    m.member_id, name, email, role,
                    r.date,
                    rt.time_id, start_at AS time_value,
                    t.theme_id, theme_name, description, thumbnail
                FROM reservation AS r
                JOIN reservation_time AS rt
                ON r.time_id = rt.time_id
                JOIN theme AS t
                ON r.theme_id = t.theme_id
                JOIN member AS m
                ON r.member_id = m.member_id
                WHERE r.reservation_id = ?
                """;
        List<Reservation> reservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER, id);
        return reservations.stream()
                .findFirst();
    }

    @Override
    public boolean existsByTimeId(final long timeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(final long themeId) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?) AS exist";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    @Override
    public boolean deleteById(final long id) {
        final String query = "DELETE FROM reservation WHERE reservation_id = ?";
        final int deleted = jdbcTemplate.update(query, id);
        return deleted > 0;
    }
}

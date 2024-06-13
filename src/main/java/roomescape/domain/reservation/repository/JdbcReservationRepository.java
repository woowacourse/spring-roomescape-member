package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final String TABLE_NAME = "reservation";
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    new Member(
                            rs.getLong("member_id"),
                            new MemberName(rs.getString("member_name")),
                            rs.getString("email"),
                            rs.getString("password"),
                            MemberRole.convert(rs.getString("role"))
                    ),
                    rs.getDate("reservation_date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    ),
                    new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("reservation_date", reservation.getDate())
                .addValue("member_id", reservation.getMember().getId())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String query = """
                SELECT r.id, r.member_id AS member_id, m.name AS member_name, m.email, m.password, m.role, r.reservation_date,
                r.time_id AS time_id, t.start_at, r.theme_id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                LEFT JOIN theme AS th
                ON r.theme_id = th.id
                LEFT JOIN member AS m
                ON r.member_id = m.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByTimeId(long timeId) {
        String query = """
                SELECT r.id, r.member_id AS member_id, m.name AS member_name, m.email, m.password, m.role, r.reservation_date,
                r.time_id AS time_id, t.start_at, r.theme_id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                LEFT JOIN theme AS th
                ON r.theme_id = th.id
                LEFT JOIN member AS m
                ON r.member_id = m.id
                WHERE r.time_id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, timeId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId) {
        String query = """
                SELECT r.id FROM reservation AS r
                WHERE EXISTS(
                SELECT 1 FROM reservation 
                WHERE r.reservation_date = ? AND r.time_id = ? AND r.theme_id = ?)              
                """;

        try {
            jdbcTemplate.queryForObject(query, Long.class, date, timeId, themeId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT r.id, r.member_id AS member_id, m.name AS member_name, m.email, m.password, m.role, r.reservation_date,
                r.time_id AS time_id, t.start_at, r.theme_id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                ON r.time_id = t.id
                LEFT JOIN theme AS th
                ON r.theme_id = th.id
                LEFT JOIN member AS m
                ON r.member_id = m.id
                """;

        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<Reservation> findByThemeIdAndMemberIdAndDateFromAndDateTo(long themeId, long memberId, LocalDate dateTo,
                                                                          LocalDate dateFrom) {
        String query = """
                SELECT r.id, r.reservation_date, r.member_id AS member_id, m.name AS member_name, m.email, m.password, m.role,
                r.time_id AS time_id, t.start_at, r.theme_id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id AND r.theme_id = ?
                INNER JOIN member AS m
                ON r.member_id = m.id AND r.member_id = ?
                WHERE r.reservation_date BETWEEN ? and ?
                """;

        return jdbcTemplate.query(query, ROW_MAPPER, themeId, memberId, dateFrom, dateTo);
    }
}

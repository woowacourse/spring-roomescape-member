package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@Repository
public class ReservationDao {
    private static final String SELECT_SQL = """
            SELECT
                r.id AS reservation_id,
                r.date AS reservation_date,
                m.id AS member_id,
                m.name AS member_name,
                m.email AS member_email,
                t.id AS time_id,
                t.start_at AS time_value,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail
            FROM reservation AS r
            INNER JOIN member AS m
            ON m.id = r.member_id
            INNER JOIN reservation_time AS t
            ON r.time_id = t.id
            INNER JOIN theme AS th
            ON r.theme_id = th.id
            """;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getDate("reservation_date").toLocalDate(),
            new Member(rs.getLong("member_id"),
                    new Name(rs.getString("member_name")),
                    rs.getString("member_email")),
            new ReservationTime(rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()),
            new RoomTheme(rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_description"),
                    rs.getString("theme_thumbnail")));

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_SQL, rowMapper);
    }

    public List<Reservation> findAllMatching(Long themeId, Long memberId,
                                             LocalDate dateFrom, LocalDate dateTo) {
        return jdbcTemplate.query(SELECT_SQL + """
                WHERE theme_id = ? AND member_id = ?
                AND date BETWEEN ? AND ?
                """, rowMapper, themeId, memberId, dateFrom, dateTo);
    }

    public boolean exists(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS
                   (SELECT 1 FROM reservation
                   WHERE date = ? AND time_id = ? AND theme_id = ?)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("member_id", reservation.getMember().getId())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Reservation(id, reservation);
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) > 0;
    }
}

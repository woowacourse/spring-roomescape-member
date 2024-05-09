package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER =
            (resultSet, rowNum) -> new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Member(
                            resultSet.getLong("member_id"),
                            new Name(resultSet.getString("member_name")),
                            resultSet.getString("email"),
                            Role.valueOf(resultSet.getString("role"))),
                    new ReservationDate(
                            LocalDate.parse(resultSet.getString("date"))),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            LocalTime.parse(resultSet.getString("start_at"))),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation create(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", reservation.getDate().date())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getMember(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public boolean removeById(Long id) {
        int updatedRowCount = jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
        return updatedRowCount == 1;
    }

    public boolean hasDuplicateReservation(Reservation reservation) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ?
                """;

        int duplicatedCount = jdbcTemplate.queryForObject(
                sql,
                Integer.class, reservation.getDate().date(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );

        return duplicatedCount > 0;
    }

    public boolean hasByTimeId(Long id) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE time_id = ?
                """;

        int hasCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return hasCount > 0;
    }

    public boolean hasByThemeId(Long id) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE theme_id = ?
                """;

        int hasCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return hasCount > 0;
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id AS reservation_id, 
                    r.date, 
                    t.id AS time_id, 
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email,
                    m.role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m
                ON r.member_id = m.id;
                """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    public List<Reservation> findByMemberThemePeriod(Long memberId, Long themeId,
                                                     LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                SELECT 
                    r.id AS reservation_id, 
                    r.date, 
                    t.id AS time_id, 
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email,
                    m.role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m 
                ON r.member_id = m.id
                WHERE m.id = ? AND th.id = ? AND r.date BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, memberId, themeId, dateFrom, dateTo);
    }

    public Reservation findById(Long id) {
        String sql = """
                SELECT 
                    r.id AS reservation_id, 
                    r.date, 
                    t.id AS time_id, 
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email,
                    m.role
                FROM reservation AS r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m 
                ON r.member_id = m.id
                WHERE r.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, RESERVATION_ROW_MAPPER, id);
    }
}

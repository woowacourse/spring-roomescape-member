package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER =
            (resultSet, rowNum) -> Reservation.afterSave(
                    resultSet.getLong("reservation_id"),
                    resultSet.getDate("date").toLocalDate(),
                    Member.afterSave(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password"),
                            Role.valueOf(resultSet.getString("member_role"))
                    ),
                    ReservationTime.afterSave(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    ),
                    Theme.afterSave(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(reservation.getDate()));
            ps.setLong(2, reservation.getMember().getId());  // member_id 추가
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();

        return Reservation.afterSave(
                generatedId,
                reservation.getDate(),
                reservation.getMember(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                 r.id as reservation_id,
                 r.date,
                 m.id as member_id,
                 m.name as member_name,
                 m.email as member_email,
                 m.password as member_password,
                 m.role as member_role,
                 rt.id as time_id,
                 rt.start_at as time_value,
                 t.id as theme_id,
                 t.name as theme_name,
                 t.description as theme_description,
                 t.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN member as m ON r.member_id = m.id
                INNER JOIN reservation_time as rt ON r.time_id = rt.id
                INNER JOIN theme as t ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<Reservation> findByMemberAndThemeAndDateRange(final Long memberId, final Long themeId, final LocalDate dateFrom, final LocalDate dateTo) {
        final String sql = """
                SELECT
                 r.id as reservation_id,
                 r.date,
                 m.id as member_id,
                 m.name as member_name,
                 m.email as member_email,
                 m.password as member_password,
                 m.role as member_role,
                 rt.id as time_id,
                 rt.start_at as time_value,
                 t.id as theme_id,
                 t.name as theme_name,
                 t.description as theme_description,
                 t.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN member as m ON r.member_id = m.id
                INNER JOIN reservation_time as rt ON r.time_id = rt.id
                INNER JOIN theme as t ON r.theme_id = t.id
                WHERE r.date BETWEEN ? AND ?
                  AND r.member_id = ?
                  AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, dateFrom, dateTo, memberId, themeId);
    }


    @Override
    public boolean isExistByReservationId(final long id) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public boolean isExistByTimeId(final long id) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme) {
        final String sql = """
                SELECT COUNT(*)
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON t.id = r.time_id
                WHERE r.date = ?
                AND t.start_at = ?
                AND r.theme_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, time, theme.getId());
        return count != null && count > 0;
    }

    @Override
    public int deleteById(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

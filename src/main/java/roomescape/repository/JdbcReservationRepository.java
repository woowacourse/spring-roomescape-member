package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member.name"),
                            resultSet.getString("member.email"),
                            resultSet.getString("member.password"),
                            resultSet.getString("member.role")
                    ),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            LocalTime.parse(resultSet.getString("reservation_time.start_at"))
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.description"),
                            resultSet.getString("theme.thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("member_id", reservation.getMemberId());
        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT *
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT *
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.id = ?
                """;

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findPopularThemesByReservation(int daysToStartBefore, int daysToEndBefore, int limit) {
        LocalDate today = LocalDate.now();
        LocalDate startDay = today.minusDays(daysToStartBefore);
        LocalDate endDay = today.minusDays(daysToEndBefore);

        String sql = """
                SELECT theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(*) DESC
                LIMIT ?;
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("theme_id"),
                startDay, endDay, limit);
    }


    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT *
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.date = ? AND th.id = ?
                 """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean existsByTimeId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existsByThemeId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<Reservation> findByConditions(
            Long themeId,
            Long memberId,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        String sql = """
                SELECT *
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE th.id = ?
                AND m.id = ?
                AND r.date BETWEEN ? AND ?
                 """;
        return jdbcTemplate.query(sql, ROW_MAPPER, themeId, memberId, dateFrom, dateTo);
    }
}

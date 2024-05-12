package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.dto.request.ReservationRequest;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> rowMapper =
            (resultSet, rowNum) -> new Reservation(
                    resultSet.getLong("id"),
                    new Member(resultSet.getLong("member_id"), resultSet.getString("member_name"),
                            resultSet.getString("member_email"), resultSet.getString("member_password")),
                    LocalDate.parse(resultSet.getString("date")),
                    new TimeSlot(resultSet.getLong("time_id"), resultSet.getString("time_value")),
                    new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
            );


    public ReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("member_id", "date", "time_id", "theme_id")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id as reservation_id,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN member as m ON r.member_id = m.id
                INNER JOIN reservation_time as t ON r.time_id = t.id
                INNER JOIN theme as th ON r.theme_id = th.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Long create(final ReservationRequest reservationRequest) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id", reservationRequest.memberId())
                .addValue("date", reservationRequest.date())
                .addValue("time_id", reservationRequest.timeId())
                .addValue("theme_id", reservationRequest.themeId());
        return jdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public void delete(final Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isExists(final LocalDate date, final Long timeId, final Long themeId) {
        String sql = """
                SELECT
                count(*)
                FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId) != 0;
    }

    public boolean isExistsTimeId(final Long timeId) {
        String sql = "select exists(select 1 from reservation where time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public boolean isExistsThemeId(final Long themeId) {
        String sql = "select exists(select 1 from reservation where theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = """
                    SELECT
                    r.id as reservation_id,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                    FROM reservation as r
                    INNER JOIN member as m ON r.member_id = m.id
                    INNER JOIN reservation_time as t ON r.time_id = t.id
                    INNER JOIN theme as th ON r.theme_id = th.id where date = ? and theme_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }
}

package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    public static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
    {
        LocalDate date = LocalDate.parse(
            resultSet.getString("date"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        return new Reservation(
            resultSet.getLong("reservation_id"),
            date,
            new ReservationTime(
                resultSet.getLong("time_id"),
                LocalTime.parse(
                    resultSet.getString("time_value"),
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            ),
            new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
            ),
            new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                resultSet.getString("member_password"),
                Role.valueOf(resultSet.getString("member_role"))
            )
        );
    };
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
            """
                    SELECT
                    r.id as reservation_id,
                    r.date,
                    rt.id as time_id,
                    rt.start_at as time_value,
                    t.id as theme_id,
                    t.name as theme_name,
                    t.description as theme_description,
                    t.thumbnail as theme_thumbnail,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    m.role as member_role
                    FROM reservation as r
                    inner join reservation_time as rt
                    on r.time_id = rt.id
                    inner join theme as t
                    on r.theme_id = t.id
                    inner join member as m
                    on r.member_id = m.id
                """,
            ROW_MAPPER
        );
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        parameters.put("member_id", reservation.getMember().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(
            id,
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme(),
            reservation.getMember()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    public boolean existsByThemeId(Long themeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, themeId);
    }

    public boolean existsByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ? and theme_id = ? and date = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId, themeId, date);
    }

    public List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        String query = "SELECT time_id FROM reservation WHERE theme_id = ? AND DATE = ?";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> resultSet.getLong("time_id"),
            themeId, date);
    }

    public List<Reservation> findByFilters(Long themeId, Long memberId, LocalDate dateFrom,
        LocalDate dateTo) {
        String query = """
            SELECT
            r.id as reservation_id,
            r.date,
            rt.id as time_id,
            rt.start_at as time_value,
            t.id as theme_id,
            t.name as theme_name,
            t.description as theme_description,
            t.thumbnail as theme_thumbnail,
            m.id as member_id,
            m.name as member_name,
            m.email as member_email,
            m.password as member_password,
            m.role as member_role
            FROM reservation as r
            inner join reservation_time as rt
            on r.time_id = rt.id
            inner join theme as t
            on r.theme_id = t.id
            inner join member as m
            on r.member_id = m.id
            WHERE r.theme_id = ?
            AND r.member_id = ?
            AND r.date BETWEEN ? AND ?  
            """;
        return jdbcTemplate.query(query, ROW_MAPPER, themeId, memberId, dateFrom, dateTo);
    }
}

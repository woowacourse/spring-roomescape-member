package roomescape.dao.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.Purpose;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTheme;
import roomescape.domain.reservation.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String findAllSql = """
                        SELECT 
                            r.id as reservation_id, 
                            r.date, 
                            t.id as time_id, 
                            t.start_at as time_value, 
                            tm.id as theme_id, 
                            tm.name as theme_name, 
                            tm.description,     
                            tm.thumbnail,
                            r.member_id as member_id,
                            m.name as member_name,
                            m.role as member_role
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t 
                        ON r.time_id = t.id 
                        INNER JOIN theme AS tm 
                        ON r.theme_id = tm.id
                        INNER JOIN member AS m
                        ON r.member_id = m.id
                """;
        return jdbcTemplate.query(findAllSql, getReservationRowMapper());
    }

    public List<Reservation> searchReservation(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                        SELECT 
                            r.id as reservation_id, 
                            r.date, 
                            t.id as time_id, 
                            t.start_at as time_value, 
                            tm.id as theme_id, 
                            tm.name as theme_name, 
                            tm.description,     
                            tm.thumbnail,
                            r.member_id as member_id,
                            m.name as member_name,
                            m.role as member_role
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t 
                        ON r.time_id = t.id 
                        INNER JOIN theme AS tm 
                        ON r.theme_id = tm.id AND tm.id = ?
                        INNER JOIN member AS m
                        ON r.member_id = m.id AND m.id = ?
                        WHERE r.date BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(sql, getReservationRowMapper(), themeId, memberId, dateFrom.toString(),
                dateTo.toString());
    }

    public Reservation insert(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("date", reservation.getDate().toString())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("member_id", reservation.getMemberId());

        Long id = insertActor.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reservation.getDate(), reservation.getTime(),
                reservation.getTheme(), reservation.getMember(), Purpose.CREATE);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean hasReservationForTimeId(Long timeId) {
        String sql = "SELECT EXISTS(SELECT * FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public Boolean hasReservationForThemeId(Long themeId) {
        String sql = "SELECT EXISTS(SELECT * FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    public Boolean hasSameReservation(String date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT * FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, numRow) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new ReservationTheme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ),
                new MemberInfo(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        Role.of(resultSet.getString("member_role"))
                ),
                Purpose.SEARCH
        );
    }
}

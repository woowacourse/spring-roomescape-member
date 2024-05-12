package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.SearchInfo;
import roomescape.reservation.provider.WhereQueryProvider;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReservationRepository {
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role"))
                    ),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    ));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getMember().getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId())
                .addValue("member_id", reservation.getMember().getId());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, m.id AS member_id, m.name AS member_name, m.email, m.password, m.role,
                r.date, rt.id AS time_id, rt.start_at AS time_value, t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                INNER JOIN member m ON r.member_id = m.id""";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Reservation findByReservationId(long reservationId) {
        String sql = """
                SELECT r.id AS reservation_id, m.id AS member_id, m.name AS member_name, m.email, m.password, m.role,
                r.date, rt.id AS time_id, rt.start_at AS time_value, t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                INNER JOIN member m ON r.member_id = m.id
                WHERE r.id = ?""";

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, reservationId);
    }

    public List<Reservation> findBySearchInfo(SearchInfo searchInfo) {
        WhereQueryProvider whereQueryProvider = new WhereQueryProvider();
        String whereQuery = whereQueryProvider.makeQuery(searchInfo);

        String sql = """
                SELECT r.id AS reservation_id, m.id AS member_id, m.name AS member_name, m.email, m.password, m.role,
                r.date, rt.id AS time_id, rt.start_at AS time_value, t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                INNER JOIN member m ON r.member_id = m.id
                """ + whereQuery;

        return jdbcTemplate.query(sql, ROW_MAPPER, whereQueryProvider.getParams());
    }

    public Integer deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    public Boolean checkExists(Reservation reservation) {
        String sql = """
                SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
                FROM reservation AS r
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON r.theme_id = t.id
                WHERE r.date = ? AND rt.id = ? AND t.id = ?""";

        String date = reservation.getDate().toString();
        Long timeId = reservation.getTime().getId();
        Long themeId = reservation.getTheme().getId();

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}

package roomescape.reservation.infrastructure;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.infrastructure.dto.ReservationDetailData.MemberData;
import roomescape.reservation.infrastructure.dto.ReservationDetailData.ThemeData;
import roomescape.reservation.infrastructure.dto.ReservationDetailData.TimeData;

@Repository
public class JdbcReservationQueryDao {
    private final static RowMapper<ReservationDetailData> RESERVATION_DETAIL_ROW_MAPPER = (rs, rowNum) -> new ReservationDetailData(
            rs.getLong("reservation_id"),
            new MemberData(
                    rs.getLong("member_id"),
                    rs.getString("member_name")
            ),
            new ThemeData(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            ),
            rs.getDate("date").toLocalDate(),
            new TimeData(
                    rs.getLong("time_id"),
                    rs.getTime("start_at").toLocalTime()
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationQueryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationDetailData> findAllReservationDetails() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.name AS member_name,
                    t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail,
                    rt.id AS time_id, rt.start_at,
                    r.date
                FROM reservation r
                JOIN members m ON r.member_id = m.id
                JOIN theme t ON r.theme_id = t.id
                JOIN reservation_time rt ON r.time_id = rt.id
                """;

        return jdbcTemplate.query(sql, RESERVATION_DETAIL_ROW_MAPPER);
    }

    public ReservationDetailData getReservationDetailById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id, m.name AS member_name,
                    t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail,
                    rt.id AS time_id, rt.start_at,
                    r.date
                FROM reservation r
                JOIN members m ON r.member_id = m.id
                JOIN theme t ON r.theme_id = t.id
                JOIN reservation_time rt ON r.time_id = rt.id
                WHERE r.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, RESERVATION_DETAIL_ROW_MAPPER,
                id);
    }
}

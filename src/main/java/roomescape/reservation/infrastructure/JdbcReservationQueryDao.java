package roomescape.reservation.infrastructure;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.presentation.controller.ReservationSearchCondition;

@Repository
public class JdbcReservationQueryDao {
    public static final RowMapper<ReservationDetailData> RESERVATION_DETAIL_DATA_ROW_MAPPER =
            (rs, rowNum) -> new ReservationDetailData(
                    rs.getLong("reservation_id"),
                    new ReservationDetailData.MemberData(
                            rs.getLong("member_id"),
                            rs.getString("member_name")
                    ),
                    new ReservationDetailData.ThemeData(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    ),
                    rs.getDate("date").toLocalDate(),
                    new ReservationDetailData.TimeData(
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

        return jdbcTemplate.query(sql, RESERVATION_DETAIL_DATA_ROW_MAPPER);
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

        return jdbcTemplate.queryForObject(sql, RESERVATION_DETAIL_DATA_ROW_MAPPER,
                id);
    }

    public List<ReservationDetailData> findByCondition(ReservationSearchCondition condition) {
        StringBuilder sql = new StringBuilder("""
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
                    WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (condition.memberName() != null) {
            sql.append(" AND m.name = ?");
            params.add(condition.memberName());
        }
        if (condition.themeId() != null) {
            sql.append(" AND t.id = ?");
            params.add(condition.themeId());
        }
        if (condition.dateFrom() != null) {
            sql.append(" AND r.date >= ?");
            params.add(condition.dateFrom());
        }
        if (condition.dateTo() != null) {
            sql.append(" AND r.date <= ?");
            params.add(condition.dateTo());
        }

        return jdbcTemplate.query(sql.toString(), RESERVATION_DETAIL_DATA_ROW_MAPPER,
                params.toArray());
    }
}

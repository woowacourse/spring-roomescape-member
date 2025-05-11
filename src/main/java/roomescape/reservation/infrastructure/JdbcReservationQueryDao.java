package roomescape.reservation.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationQueryDao(NamedParameterJdbcTemplate jdbcTemplate) {
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

    public Optional<ReservationDetailData> findReservationDetailById(Long id) {
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
                WHERE r.id = :id
                """;

        Map<String, Long> namedParams = Map.of("id", id);

        try {
            ReservationDetailData reservationDetailData =
                    jdbcTemplate.queryForObject(sql, namedParams, RESERVATION_DETAIL_DATA_ROW_MAPPER);
            return Optional.of(reservationDetailData);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ReservationDetailData> findByCondition(ReservationSearchCondition condition) {
        SqlBuilder sql = new SqlBuilder("""
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

        sql.and("m.id = :memberId", "memberId", condition.memberId())
                .and("t.id = :themeId", "themeId", condition.themeId())
                .and("r.date >= :dateFrom", "dateFrom", condition.dateFrom())
                .and("r.date <= :dateTo", "dateTo", condition.dateTo());

        return jdbcTemplate.query(
                sql.getSql(),
                sql.getParams(),
                RESERVATION_DETAIL_DATA_ROW_MAPPER
        );
    }
}

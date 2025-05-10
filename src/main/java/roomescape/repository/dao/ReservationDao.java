package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@Component
@RequiredArgsConstructor
public class ReservationDao {

    private static final RowMapper<Reservation> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password"),
                    resultSet.getString("member_role")
            ),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new ReservationTheme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("th_name"),
                    resultSet.getString("th_description"),
                    resultSet.getString("th_thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public List<Reservation> selectAll() {
        String selectAllQuery = """
                SELECT r.id, r.date, r.time_id, r.theme_id,
                        m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role,
                        rt.start_at, th.name AS th_name,
                        th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN member m ON r.member_id = m.id
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(selectAllQuery, DEFAULT_ROW_MAPPER);
    }

    public Reservation insertAndGet(Reservation reservation) {
        String insertQuery = "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setLong(1, reservation.member().id());
            ps.setObject(2, reservation.date());
            ps.setLong(3, reservation.time().id());
            ps.setLong(4, reservation.theme().id());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        return reservation.withId(id);
    }

    public Optional<Reservation> selectById(Long id) {
        String selectQuery = """
                SELECT r.id, r.date, r.time_id, r.theme_id,
                        m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role,
                        rt.start_at, th.name AS th_name,
                        th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN member m ON r.member_id = m.id
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteQuery, id);
    }

    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        String existQuery = """
                SELECT EXISTS (
                        SELECT 1
                        FROM reservation
                        WHERE time_id = ? AND date = ? AND theme_id = ?)
                """;
        return Optional.ofNullable(
                        jdbcTemplate.queryForObject(existQuery, Boolean.class, timeId, date, themeId))
                .orElse(false);
    }

    public List<Reservation> selectAllByThemeIdAndMemberIdInDateRange(Long themeId, Long memberId, LocalDate dateFrom,
            LocalDate dateTo) {
        String baseQuery = """
                SELECT r.id, r.date, r.time_id, r.theme_id,
                        m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role,
                        rt.start_at, th.name AS th_name,
                        th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN member m ON r.member_id = m.id
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;
        Map<String, Object> whereParams = new HashMap<>();
        whereParams.put("r.theme_id = ?", themeId);
        whereParams.put("r.member_id = ?", memberId);
        whereParams.put("? <= r.date", dateFrom);
        whereParams.put("r.date <= ?", dateTo);
        List<Object> params = new ArrayList<>();
        String selectQuery = generateQueryAndParameters(baseQuery, whereParams, params);

        return jdbcTemplate.query(selectQuery, DEFAULT_ROW_MAPPER, params.toArray());
    }

    private String generateQueryAndParameters(String baseQuery, Map<String, Object> whereParams,
            List<Object> outputParams) {
        List<String> whereClauses = new ArrayList<>();
        for (Map.Entry<String, Object> entry : whereParams.entrySet()) {
            if (entry.getValue() != null) {
                whereClauses.add(entry.getKey());
                outputParams.add(entry.getValue());
            }
        }
        if (whereClauses.isEmpty()) {
            return baseQuery;
        }
        String whereClause = "WHERE " + String.join(" AND ", whereClauses);
        return baseQuery + "\n" + whereClause;
    }
}

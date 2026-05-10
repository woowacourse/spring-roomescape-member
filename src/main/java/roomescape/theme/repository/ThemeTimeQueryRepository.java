package roomescape.theme.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.theme.service.dto.ThemeTimeAvailability;

@Repository
@RequiredArgsConstructor
public class ThemeTimeQueryRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final RowMapper<ThemeTimeAvailability> availableReservationTimeRowMapper =
            (rs, rowNum) -> new ThemeTimeAvailability(
                    rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime(),
                    rs.getBoolean("available")
            );

    public List<ThemeTimeAvailability> findThemeAvailableTime(long themeId, LocalDate date) {
        final String sql = """
                    SELECT
                          rt.id,
                          rt.start_at,
                          CASE WHEN r.id IS NULL THEN TRUE ELSE FALSE END AS available
                      FROM reservation_time rt
                      LEFT JOIN reservation r
                          ON rt.id = r.time_id
                           AND r.theme_id = ?
                           AND r.date = ?
                    """;
        return jdbcTemplate.query(sql, availableReservationTimeRowMapper, themeId, date.format(DATE_FORMATTER));
    }

}

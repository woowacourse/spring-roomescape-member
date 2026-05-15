package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationHistory;

@Repository
@RequiredArgsConstructor
public class ReservationHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public void save(ReservationHistory history) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", history.username())
                .addValue("date", history.reservationDate())
                .addValue("start_at", history.startAt())
                .addValue("theme_name", history.themeName());

        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_history")
                .usingColumns("name", "date", "start_at", "theme_name")
                .execute(params);
    }
}

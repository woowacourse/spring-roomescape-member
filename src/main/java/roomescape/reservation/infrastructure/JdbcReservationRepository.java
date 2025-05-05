package roomescape.reservation.infrastructure;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = Map.of(
                "member_id", reservation.getMemberId(),
                "date", Date.valueOf(reservation.getReservationDate()),
                "time_id", reservation.getReservationTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM reservation WHERE id=?";
        jdbcTemplate.update(deleteSql, id);
    }

    @Override
    public boolean existsDuplicatedReservation(LocalDate date, Long timeId, Long themeId) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM reservation
                WHERE theme_id = ? AND time_id = ? AND date = ?
            )
        """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId, timeId, date);
    }
}

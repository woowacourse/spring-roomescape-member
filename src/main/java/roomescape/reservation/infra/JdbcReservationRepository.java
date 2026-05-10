package roomescape.reservation.infra;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationDetail;
import roomescape.reservation.domain.repository.ReservationRepository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationDetail> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT r.id, r.name, r.date, r.theme_id, t.name as theme_name, t.description, t.thumbnail_img_url, r.time_id, rt.start_at
                        FROM reservation r
                        JOIN theme t ON r.theme_id = t.id
                        JOIN reservation_time rt ON r.time_id = rt.id
                        ORDER BY r.date ASC
                        """,
                (rs, rowNum) ->
                        new ReservationDetail(rs.getLong("id"),
                                rs.getString("name"),
                                rs.getDate("date").toLocalDate(),
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("description"),
                                rs.getString("thumbnail_img_url"),
                                rs.getLong("time_id"),
                                rs.getTime("start_at").toLocalTime())
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("time_id", reservation.getTimeId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.withId(id);
    }

    @Override
    public Integer delete(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    @Override
    public Boolean existsByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND theme_id = ? AND time_id = ?)",
                Boolean.class,
                date,
                themeId,
                timeId);
    }
}

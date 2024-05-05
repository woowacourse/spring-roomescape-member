package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate template;

    public InMemoryReservationRepository(final NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                        SELECT
                            r.id AS reservation_id, r.name AS reservation_name, r.date AS reservation_date, 
                            rt.id AS time_id, rt.start_at AS reservation_time, 
                            th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail 
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS rt on r.time_id = rt.id 
                        INNER JOIN theme AS th ON r.theme_id = th.id
                """;

        return template.query(sql, itemRowMapper());
    }

    private RowMapper<Reservation> itemRowMapper() {
        return ((rs, rowNum) -> Reservation.of(
                rs.getLong("reservation_id"),
                rs.getString("reservation_name"),
                rs.getDate("reservation_date").toLocalDate(),
                new ReservationTime(rs.getLong("time_id"), rs.getTime("reservation_time").toLocalTime()),
                Theme.of(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_description"),
                        rs.getString("theme_thumbnail"))
        ));
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (:name, :date, :timeId, :themeId)";
        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservation.getClientName().value())
                .addValue("date", reservation.getDate().value())
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        final long savedReservationId = keyHolder.getKey().longValue();

        return reservation.initializeIndex(savedReservationId);
    }

    @Override
    public int deleteById(final Long reservationId) {
        final String sql = "DELETE FROM reservation WHERE id = :id";
        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", reservationId);

        return template.update(sql, param);
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = :date AND time_id = :timeId AND :themeId)";
        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public boolean existByTimeId(final Long reservationTimeId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = :timeId)";
        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("timeId", reservationTimeId);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = :themeId)";
        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("themeId", themeId);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                    SELECT
                        r.id AS reservation_id, r.name AS reservation_name, r.date AS reservation_date, 
                        rt.id AS time_id, rt.start_at AS reservation_time, 
                        th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail 
                    FROM reservation AS r 
                    INNER JOIN reservation_time AS rt ON r.time_id = rt.id 
                    INNER JOIN theme AS th ON r.theme_id = th.id 
                    WHERE date = :date AND theme_id = :themeId
                """;

        final MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return template.query(sql, param, itemRowMapper());
    }
}

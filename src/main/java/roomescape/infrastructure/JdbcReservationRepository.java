package roomescape.infrastructure;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> Reservation.afterSave(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            ReservationTime.afterSave(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
            ),
            Theme.afterSave(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();

        return Reservation.afterSave(
                generatedId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        try {
            final String sql = """
                    SELECT
                     r.id as reservation_id,
                     r.name,
                     r.date,
                     rt.id as time_id,
                     rt.start_at as time_value,
                     t.id as theme_id,
                     t.name as theme_name,
                     t.description as theme_description,
                     t.thumbnail as theme_thumbnail
                    FROM reservation as r
                    INNER JOIN reservation_time as rt ON r.time_id = rt.id
                    INNER JOIN theme as t ON r.theme_id = t.id
                    WHERE r.id = ?
                    """;
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                 r.id as reservation_id,
                 r.name,
                 r.date,
                 rt.id as time_id,
                 rt.start_at as time_value,
                 t.id as theme_id,
                 t.name as theme_name,
                 t.description as theme_description,
                 t.thumbnail as theme_thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as rt ON r.time_id = rt.id
                INNER JOIN theme as t ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existById(final long id) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public boolean existByTimeId(final long timeId) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existByThemeId(final long themeId) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }

    @Override
    public boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme) {
        final String sql = """
                SELECT COUNT(*)
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON t.id = r.time_id
                WHERE r.date = ?
                AND t.start_at = ?
                AND r.theme_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, time, theme.getId());
        return count != null && count > 0;
    }

    @Override
    public void deleteById(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

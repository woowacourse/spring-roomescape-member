package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcRepository(
            @Autowired final JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(
            final Reservation reservation,
            final Long reservationTimeId,
            final Long themeId
    ) {
        final Map<String, Object> reservationParameter = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservationTimeId,
                "theme_id", themeId
        );
        return simpleJdbcInsert
                .executeAndReturnKey(reservationParameter)
                .longValue();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id AS id, 
                       r.name AS name, 
                       r.date AS date, 
                       t.id AS time_id, 
                       t.start_at AS time_value, 
                       th.id AS theme_id, 
                       th.name AS theme_name, 
                       th.description AS theme_description, 
                       th.thumbnail AS theme_thumbnail 
                FROM reservation AS r INNER JOIN reservation_time AS t INNER JOIN theme AS th
                    ON r.time_id = t.id AND r.theme_id = th.id  
                """;
        return jdbcTemplate.query(sql, getRowMapper());
    }

    @Override
    public Reservation findById(Long id) {
        final String sql = """ 
                SELECT r.id AS id, 
                       r.name AS name, 
                       r.date AS date, 
                       t.id AS time_id, 
                       t.start_at AS time_value, 
                       th.id AS theme_id, 
                       th.name AS theme_name, 
                       th.description AS theme_description, 
                       th.thumbnail AS theme_thumbnail 
                FROM reservation AS r 
                    INNER JOIN reservation_time AS t 
                    INNER JOIN theme AS th
                    ON r.time_id = t.id AND r.theme_id = th.id  
                WHERE r.id=? 
                """;
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    @Override
    public void delete(final Long id) {
        final String sql = "DELETE FROM reservation WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean existsById(final Long id) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean existsByReservationTime(final Long reservationTimeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    @Override
    public Boolean existsByTheme(final Long themeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public Boolean existsByReservationTimeIdAndDate(final Long reservationTimeId, final LocalDate date) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id=? AND date=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTimeId, date);
    }

    private RowMapper<Reservation> getRowMapper() {
        return (resultSet, rowNum) -> {
            final ReservationTime time = new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
            );
            final Theme theme = new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            );

            return new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    time,
                    theme
            );
        };
    }
}

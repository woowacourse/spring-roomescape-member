package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String FIND_RESERVATION_BY_ID = """
                SELECT
                    r.id as reservation_id, 
                    r.name, r.date, 
                    t.id as reservation_time_id,
                    t.start_at as time_value,
                    th.id as reservation_theme_id,
                    th.name as reservation_theme_name,
                    th.description as reservation_theme_description,
                    th.image_url as reservation_theme_image_url
            
                FROM reservation as r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id 
            
                INNER JOIN theme AS th
                ON r.theme_id = th.id
            
                WHERE r.id = ?
            """;
    private static final String FIND_ALL_RESERVATIONS = """
                SELECT r.id AS reservation_id,
                r.name, r.date, 
                t.id AS reservation_time_id,
                t.start_at AS time_value,
                th.id AS reservation_theme_id,
                th.name AS reservation_theme_name,
                th.description AS reservation_theme_description,
                th.image_url AS reservation_theme_image_url
            
                FROM reservation AS r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id 
            
                INNER JOIN theme as th
                ON r.theme_id = th.id
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(reservation)
        ).longValue();

        return new Reservation(
                generatedKey,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_RESERVATIONS,
                getReservationRowMapper()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        List<Reservation> results = jdbcTemplate.query(
                FIND_RESERVATION_BY_ID,
                getReservationRowMapper(),
                id
        );
        return results.stream()
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE date = ?
                AND time_id = ?
                AND theme_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }

    private static RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            ReservationTime time = new ReservationTime(
                    resultSet.getLong("reservation_time_id"),
                    resultSet.getObject("time_value", LocalTime.class)
            );

            Theme theme = new Theme(
                    resultSet.getLong("reservation_theme_id"),
                    resultSet.getString("reservation_theme_name"),
                    resultSet.getString("reservation_theme_description"),
                    resultSet.getString("reservation_theme_image_url")
            );
            return new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    resultSet.getObject("date", LocalDate.class),
                    time,
                    theme
            );
        };
    }
}

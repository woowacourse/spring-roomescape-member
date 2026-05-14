package roomescape.domain.reservation.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.ReservationTime;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private static final String FIND_ALL_RESERVATIONS_WITH_TIME_QUERY = """
            SELECT
                r.id AS reservation_id,
                r.username AS username,
                r.date,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail_url AS theme_thumbnail_url,
                rt.id AS time_id,
                rt.start_at
            FROM reservation AS r
            INNER JOIN reservation_time AS rt
                ON r.time_id = rt.id
            INNER JOIN theme AS t
                ON r.theme_id = t.id
            """;

    private static final String FIND_RESERVATIONS_BY_USERNAME_QUERY = FIND_ALL_RESERVATIONS_WITH_TIME_QUERY + """
            WHERE r.username = :username
            """;

    private static final String DELETE_RESERVATION_BY_ID_QUERY = """
            DELETE FROM reservation
            WHERE id = :id
            """;

    private static final String EXISTS_RESERVATION_QUERY = """
            SELECT COUNT(*)
            FROM reservation
            WHERE theme_id = :themeId
              AND date = :date
              AND time_id = :timeId
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            DataSource dataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_RESERVATIONS_WITH_TIME_QUERY,
                reservationWithTimeRowMapper()
        );
    }

    @Override
    public List<Reservation> findByUsername(String username) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username);

        return jdbcTemplate.query(
                FIND_RESERVATIONS_BY_USERNAME_QUERY,
                parameters,
                reservationWithTimeRowMapper()
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("reservation이 null 입니다.");
        }

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", reservation.getUsername())
                .addValue("theme_id", reservation.getTheme().getId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());

        Long generatedId = simpleJdbcInsert.executeAndReturnKey(parameters)
                .longValue();

        return new Reservation(
                generatedId,
                reservation.getUsername(),
                reservation.getTheme(),
                reservation.getDate(),
                reservation.getTime()
        );
    }

    @Override
    public void deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(
                DELETE_RESERVATION_BY_ID_QUERY,
                parameters
        );
    }

    @Override
    public boolean exists(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("themeId", reservation.getTheme().getId())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId());

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_RESERVATION_QUERY,
                parameters,
                Integer.class
        );

        return count != null && count > 0;
    }

    private RowMapper<Reservation> reservationWithTimeRowMapper() {
        return (resultSet, rowNumber) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("username"),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail_url")
                ),
                LocalDate.parse(resultSet.getString("date")),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        LocalTime.parse(resultSet.getString("start_at"))
                )
        );
    }
}

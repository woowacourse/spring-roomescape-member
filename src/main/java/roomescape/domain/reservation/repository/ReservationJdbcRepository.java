package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.theme.entity.Theme;

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

    private static final String DELETE_RESERVATION_BY_ID_QUERY = """
            DELETE FROM reservation
            WHERE id = :id
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_RESERVATIONS_WITH_TIME_QUERY,
                reservationWithTimeRowMapper());
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

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        Long generatedId = key.longValue();

        reservation.assignId(generatedId);

        return reservation;
    }

    @Override
    public void deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(
                DELETE_RESERVATION_BY_ID_QUERY,
                parameters);
    }

    private RowMapper<Reservation> reservationWithTimeRowMapper() {
        return (resultSet, rowNumber) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("username"),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail_url")),
                resultSet.getObject("date", LocalDate.class),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getObject("start_at", LocalTime.class)));
    }
}

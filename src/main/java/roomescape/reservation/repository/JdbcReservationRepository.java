package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.date.domain.ReservationDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNumber) -> Reservation.load(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            ReservationDate.load(
                    resultSet.getLong("date_id"),
                    resultSet.getDate("date").toLocalDate()
            ),
            ReservationTime.load(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            Theme.load(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url"),
                    resultSet.getBoolean("is_active")
            ),
            ReservationStatus.valueOf(resultSet.getString("status"))
    );

    public JdbcReservationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.status,
                    d.id as date_id,
                    d.date,
                    t.id as time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail_url,
                    th.is_active
                FROM reservation r
                INNER JOIN reservation_date d ON r.date_id = d.id
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.status,
                    d.id as date_id,
                    d.date,
                    t.id as time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail_url,
                    th.is_active
                FROM reservation r
                INNER JOIN reservation_date d ON r.date_id = d.id
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, reservationRowMapper));
    }

    @Override
    public List<Reservation> findAllByNameOrderByDateAndTime(String name) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.status,
                    d.id AS date_id,
                    d.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail_url,
                    th.is_active
                FROM reservation r
                INNER JOIN reservation_date d ON r.date_id = d.id
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.name = :name
                ORDER BY d.date ASC, t.start_at ASC
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("name", name);

        return jdbcTemplate.query(sql, params, reservationRowMapper);
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date_id", reservation.date().id())
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.theme().id())
                .addValue("status", reservation.status().name());
        Long savedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Reservation.load(
                savedId,
                reservation.name(),
                reservation.date(),
                reservation.time(),
                reservation.theme(),
                reservation.status()
        );
    }

    @Override
    public boolean existsByDateAndTimeAndThemeId(Long dateId, Long timeId, Long themeId) {
        String sql = """
                SELECT COUNT(*) 
                FROM reservation 
                WHERE date_id = :date_id
                    AND time_id = :time_id
                    AND theme_id = :theme_id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date_id", dateId)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNameAndDateAndTime(String name, Long dateId, Long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation 
                WHERE name = :name 
                  AND date_id = :date_id 
                  AND time_id = :time_id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date_id", dateId)
                .addValue("time_id", timeId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByDateId(Long dateId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation 
                WHERE date_id = :date_id 
                """;

        Integer count = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("date_id", dateId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation 
                WHERE time_id = :time_id 
                """;

        Integer count = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("time_id", timeId), Integer.class);
        return count != null && count > 0;
    }

    public boolean updateStatus(Reservation reservation) {
        String sql = "UPDATE RESERVATION SET status = :status WHERE id = :id ";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", reservation.id())
                .addValue("status", reservation.status().name());
        int updatedCount = jdbcTemplate.update(sql, params);
        return updatedCount > 0;
    }

}

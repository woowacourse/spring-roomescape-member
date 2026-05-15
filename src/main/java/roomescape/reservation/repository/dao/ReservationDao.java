package roomescape.reservation.repository.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.repository.entity.ReservationEntity;

@Repository
public class ReservationDao {

    private static final RowMapper<ReservationEntity> reservationRowMapper = (rs, rowNum) ->
            new ReservationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getObject("date", LocalDate.class),
                    rs.getLong("time_id"),
                    rs.getLong("theme_id"),
                    rs.getBoolean("is_cancelled")
            );

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationEntity> findAll() {
        String sql = "SELECT * FROM reservation WHERE is_deleted = FALSE";
        return namedParameterJdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<ReservationEntity> findById(Long id) {
        String sql = "SELECT * FROM reservation WHERE id = :id AND is_deleted = FALSE";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(sql, parameters, reservationRowMapper).stream()
                .findAny();
    }

    public List<ReservationEntity> findByName(String name) {
        String sql = "SELECT * FROM reservation WHERE name = :name AND is_deleted = FALSE";
        MapSqlParameterSource parameters = new MapSqlParameterSource("name", name);
        return namedParameterJdbcTemplate.query(sql, parameters, reservationRowMapper);
    }

    public Long save(String name, LocalDate date, Long timeId, Long themeId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId)
                .addValue("is_deleted", false)
                .addValue("is_cancelled", false);
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public int update(Long id, String name, LocalDate date, Long timeId, Long themeId) {
        String sql = """
            UPDATE reservation
            SET name = :name,
                date = :date,
                time_id = :time_id,
                theme_id = :theme_id
            WHERE id = :id
            """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);

        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public int updateCancelledById(Long id, boolean status) {
        String sql = "UPDATE reservation SET is_cancelled = :status WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("status", status)
                .addValue("id", id);
        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public int deleteById(Long id) {
        String sql = "UPDATE reservation SET is_deleted = TRUE WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public boolean existsValidReservationAt(Long themeId, LocalDate date, Long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE theme_id = :themeId
                  AND date = :date
                  AND time_id = :timeId
                  AND is_cancelled = FALSE
                  AND is_deleted = FALSE
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date)
                .addValue("timeId", timeId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

        return count != null && count > 0;
    }

    public List<ReservationEntity> findAllOnOrAfter(LocalDate localDate) {
        String sql = """
                SELECT * 
                FROM reservation
                WHERE date >= :date
                  AND is_deleted = FALSE
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource("date", localDate);
        return namedParameterJdbcTemplate.query(sql, parameters, reservationRowMapper);
    }
}

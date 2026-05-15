package roomescape.time.repository.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
public class ReservationTimeDao {

    private static final RowMapper<ReservationTimeEntity> reservationTimeRowMapper = (rs, rowNum) ->
            new ReservationTimeEntity(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(LocalTime startAt) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startAt", startAt)
                .addValue("is_deleted", false);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public Optional<ReservationTimeEntity> findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = :id AND is_deleted = FALSE";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(sql, parameters, reservationTimeRowMapper)
                .stream()
                .findFirst();
    }

    public ReservationTimeEntity getByIdIncludingDeleted(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, reservationTimeRowMapper);
    }

    public List<ReservationTimeEntity> findAll() {
        String sql = "SELECT * FROM reservation_time WHERE is_deleted = FALSE";
        return namedParameterJdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public int deleteById(Long id) {
        String sql = "UPDATE reservation_time SET is_deleted = TRUE WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.update(sql, parameters);
    }

    public List<Long> findReservedTimeIds(Long themeId, LocalDate date) {
        String sql = """
                 SELECT time_id
                 FROM reservation 
                 WHERE theme_id = :themeId
                   AND date = :date
                   AND is_deleted = FALSE
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date.toString());

        return namedParameterJdbcTemplate.queryForList(sql, parameters, Long.class);
    }

    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation_time
                WHERE id = :id
                  AND is_deleted = FALSE
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);

        return count != null && count > 0;
    }
}

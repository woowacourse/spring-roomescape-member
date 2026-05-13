package roomescape.time.repository.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(LocalTime startAt) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startAt", startAt)
                .addValue("is_deleted", false);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public Optional<ReservationTimeEntity> findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper, id)
                .stream()
                .findFirst();
    }

    public ReservationTimeEntity getById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
    }

    public List<ReservationTimeEntity> findAll() {
        String sql = "SELECT * FROM reservation_time WHERE is_deleted = FALSE;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public int deleteById(Long id) {
        String sql = "UPDATE reservation_time SET is_deleted = TRUE WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public List<Long> findReservedTimeIds(Long themeId, LocalDate date) {
        String sql = """
                 SELECT time_id
                 FROM reservation 
                 WHERE theme_id = ? AND date = ? AND is_deleted = FALSE;
                """;

        return jdbcTemplate.queryForList(sql, Long.class, themeId, date.toString());
    }

    public boolean existsById(Long id) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation_time
                WHERE id = ? AND is_deleted = FALSE;
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                id
        );

        return count != null && count > 0;
    }
}

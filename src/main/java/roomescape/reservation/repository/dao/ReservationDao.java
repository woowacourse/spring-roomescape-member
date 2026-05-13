package roomescape.reservation.repository.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
                    rs.getLong("theme_id")
            );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationEntity> findAll() {
        String sql = "select * from reservation;";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Long insert(String name, LocalDate date, Long timeId, Long themeId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsReservationAt(Long themeId, LocalDate date, Long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE theme_id = ?
                  AND date = ?
                  AND time_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                themeId,
                date,
                timeId
        );

        return count != null && count > 0;
    }
}

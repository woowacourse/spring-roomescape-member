package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.ReservationTimeNotFoundException;

@Repository
public class ReservationTimeRepository {

    private static final RowMapper<ReservationTime> reservationTimeRowMapper = (rs, rowNum) ->
            ReservationTime.from(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return ReservationTime.from(id, reservationTime.getStartAt());
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select count(1) from reservation_time where start_at = :startAt;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startAt", startAt);
        Integer count = jdbcTemplate.queryForObject(sql, parameters, Integer.class);
        return count != null && count > 0;
    }

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time order by start_at;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation_time where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedCount = jdbcTemplate.update(sql, parameters);

        if (deletedCount == 0) {
            throw new ReservationTimeNotFoundException("존재하지 않는 ID입니다");
        }
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(sql, parameters, reservationTimeRowMapper)
                .stream()
                .findFirst();
    }

    public List<Long> findIdByCondition(Long themeId, LocalDate date) {
        String sql = "select time_id " +
                "from reservation " +
                "where theme_id = :themeId and date = :date;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date);

        return jdbcTemplate.queryForList(sql, parameters, Long.class);
    }
}

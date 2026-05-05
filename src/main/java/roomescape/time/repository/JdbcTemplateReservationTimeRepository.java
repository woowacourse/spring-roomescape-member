package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;

@Repository
public class JdbcTemplateReservationTimeRepository implements ReservationTimeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (resultSet, rowNum) -> ReservationTime.of(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    public JdbcTemplateReservationTimeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id=:id";

        SqlParameterSource params = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, params, RESERVATION_TIME_ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource("start_at", reservationTime.startAt());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int deleteCount = jdbcTemplate.update(sql, params);
        if (deleteCount == 0) {
            throw new NotFoundException("예약 시간을 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = :start_at";
        MapSqlParameterSource params = new MapSqlParameterSource("start_at", startAt);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}

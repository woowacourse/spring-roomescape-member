package roomescape.domain.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;

import roomescape.domain.time.ReservationTime;
import roomescape.global.query.QueryBuilder;
import roomescape.global.query.SelectQuery;
import roomescape.global.query.condition.ComparisonCondition;
import roomescape.global.query.condition.MultiLineCondition;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final String TABLE_NAME = "reservation_time";
    private static final RowMapper<ReservationTime> ROW_MAPPER = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new ReservationTime(id, reservationTime);
    }

    @Override
    public boolean existsByStartAt(LocalTime localTime) {
        SelectQuery subQuery = QueryBuilder.select(TABLE_NAME)
                .addColumns("1")
                .where(ComparisonCondition.equalTo("start_at", localTime));
        String query = QueryBuilder.select(TABLE_NAME)
                .addColumns("id")
                .where(MultiLineCondition.exists(subQuery))
                .build();
        try {
            jdbcTemplate.queryForObject(query, Long.class);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        String query = QueryBuilder.select(TABLE_NAME)
                .addAllColumns()
                .where(ComparisonCondition.equalTo("id", id))
                .build();
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = QueryBuilder.select(TABLE_NAME)
                .addAllColumns()
                .build();
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public void deleteById(long id) {
        String query = QueryBuilder.delete(TABLE_NAME)
                .where(ComparisonCondition.equalTo("id", id))
                .build();
        jdbcTemplate.update(query);
    }
}

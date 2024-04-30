package roomescape.domain.reservation.repository;

import java.time.LocalDate;
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

import roomescape.domain.reservation.Reservation;
import roomescape.domain.time.ReservationTime;
import roomescape.global.query.QueryBuilder;
import roomescape.global.query.SelectQuery;
import roomescape.global.query.condition.ComparisonCondition;
import roomescape.global.query.condition.MultiLineCondition;
import roomescape.global.query.join.JoinCondition;
import roomescape.global.query.join.JoinType;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final String TABLE_NAME = "reservation";
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("reservation_date").toLocalDate(),
            new ReservationTime(rs.getLong("time_id"), rs.getTime("start_at").toLocalTime())
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("reservation_date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation);
    }

    @Override
    public boolean existsByReservationDateTime(LocalDate date, long timeId) {
        SelectQuery subQuery = QueryBuilder.select(TABLE_NAME)
                .addColumns("1")
                .where(ComparisonCondition.equalTo("reservation_date", date))
                .where(ComparisonCondition.equalTo("time_id", timeId));
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
    public Optional<Reservation> findById(long id) {
        String query = QueryBuilder.select(TABLE_NAME)
                .alias("r")
                .addAllColumns()
                .join(JoinType.INNER, "reservation_time", JoinCondition.on("r.time_id", "t.id"), "t")
                .where(ComparisonCondition.equalTo("r.id", id))
                .build();
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String query = QueryBuilder.select(TABLE_NAME)
                .alias("r")
                .addAllColumns()
                .join(JoinType.INNER, "reservation_time", JoinCondition.on("r.time_id", "t.id"), "t")
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

package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationTimeRepository;

@Repository
public class ReservationTimeDAO implements ReservationTimeRepository {

    private static final String TABLE_NAME = "reservation_time";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationTimeDAO(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> reservationTimeOf(resultSet)
        );
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = :id";

        Map<String, Long> params = Map.of("id", id);

        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql,
                    params,
                    (resultSet, rowNum) -> reservationTimeOf(resultSet)
            );

            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }
    }

    private ReservationTime reservationTimeOf(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.getStartAt() == null) {
            throw new IllegalArgumentException("start_at cannot be null");
        }

        if (reservationTime.existId()) {
            return update(reservationTime);
        }

        return create(reservationTime);
    }

    private ReservationTime update(ReservationTime reservationTime) {
        String sql = "update reservation_time set start_at = :start_at where id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("start_at", reservationTime.getStartAt());
        params.put("id", reservationTime.getId());

        int updateRowCount = jdbcTemplate.update(sql, params);

        if (updateRowCount == 0) {
            throw new EntityNotFoundException("ReservationTime with id " + reservationTime.getId() + " not found");
        }

        return reservationTime;
    }

    private ReservationTime create(ReservationTime reservationTime) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("start_at",
                reservationTime.getStartAt());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "delete from reservation_time where id = :id";
        Map<String, Long> params = Map.of("id", id);

        int deleteRowCount = jdbcTemplate.update(deleteSql, params);

        if (deleteRowCount != 1) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }
    }
}

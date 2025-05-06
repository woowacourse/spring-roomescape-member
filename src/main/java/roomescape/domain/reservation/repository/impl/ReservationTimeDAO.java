package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
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
    public ReservationTimeDAO(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "select * from reservation_time";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> reservationTimeOf(resultSet));
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String sql = "select * from reservation_time where id = :id";

        final Map<String, Long> params = Map.of("id", id);

        try {
            final ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, params,
                    (resultSet, rowNum) -> reservationTimeOf(resultSet));
            return Optional.ofNullable(reservationTime);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private ReservationTime reservationTimeOf(final ResultSet resultSet) throws SQLException {
        return ReservationTime.builder()
                .id(resultSet.getLong("id"))
                .startAt(LocalTime.parse(resultSet.getString("start_at")))
                .build();
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        if (reservationTime.existId()) {
            return update(reservationTime);
        }

        return create(reservationTime);
    }

    private ReservationTime update(final ReservationTime reservationTime) {
        final String sql = "update reservation_time set start_at = :start_at where id = :id";

        final Map<String, Object> params = Map.of("start_at", reservationTime.getStartAt(), "id",
                reservationTime.getId());

        final int updateRowCount = jdbcTemplate.update(sql, params);

        if (updateRowCount == 0) {
            throw new EntityNotFoundException("ReservationTime with id " + reservationTime.getId() + " not found");
        }

        return reservationTime;
    }

    private ReservationTime create(final ReservationTime reservationTime) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("start_at",
                reservationTime.getStartAt());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public void deleteById(final Long id) {
        final String deleteSql = "delete from reservation_time where id = :id";
        final Map<String, Long> params = Map.of("id", id);

        final int deleteRowCount = jdbcTemplate.update(deleteSql, params);

        if (deleteRowCount != 1) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }
    }
}

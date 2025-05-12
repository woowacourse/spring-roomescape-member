package roomescape.reservation.repository;

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

import roomescape.reservation.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private static final String TABLE_NAME = "reservation_time";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationTimeDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> reservationTimeOf(resultSet)
        );
    }

    public Optional<ReservationTime> findById(final Long id) {
        String sql = "select * from reservation_time where id = :id";

        Map<String, Long> params = Map.of("id", id);

        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql,
                    params,
                    (resultSet, rowNum) -> reservationTimeOf(resultSet)
            );

            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private ReservationTime reservationTimeOf(final ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    public int deleteById(final Long id) {
        String deleteSql = "delete from reservation_time where id = :id";
        Map<String, Long> params = Map.of("id", id);

        return jdbcTemplate.update(deleteSql, params);
    }

    public boolean existsByStartAt(final LocalTime startAt) {
        String existsSql = "select count(*) from reservation_time where start_at = :startAt";
        Map<String, LocalTime> params = Map.of("startAt", startAt);

        int count = jdbcTemplate.queryForObject(existsSql, params, Integer.class);
        return count != 0;
    }
}

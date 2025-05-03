package roomescape.reservationTime.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.Dao;
import roomescape.reservationTime.domain.ReservationTime;

@Repository
public class ReservationTimeDao implements Dao<ReservationTime> {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationTimeDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return namedParameterJdbcTemplate.query(sql, (resultSet, rowNum) -> createReservationTime(resultSet));
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        String sql = "select id, start_at from reservation_time where id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createReservationTime(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public ReservationTime add(final ReservationTime time) {
        Map<String, Object> parameters = Map.of("start_at", time.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(id, time.getStartAt());
    }

    @Override
    public void deleteById(final Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private ReservationTime createReservationTime(final ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}

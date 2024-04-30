package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {
    private final NamedParameterJdbcTemplate template;

    public H2ReservationTimeRepository(final NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";

        return template.query(sql, itemRowMapper());
    }

    private RowMapper<ReservationTime> itemRowMapper() {
        return ((rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                rs.getTime("start_at").toLocalTime()
        ));
    }

    @Override
    public Optional<ReservationTime> findById(final Long reservationTimeId) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";
        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", reservationTimeId);
            ReservationTime reservationTime = template.queryForObject(sql, param, itemRowMapper());

            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time(start_at) VALUES(:startAt)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(reservationTime);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long savedReservationTimeId = keyHolder.getKey().longValue();

        return reservationTime.initializeIndex(savedReservationTimeId);
    }

    @Override
    public void deleteById(final Long reservationTimeId) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", reservationTimeId);
        template.update(sql, param);
        System.out.println("param = " + param);
    }

    @Override
    public Optional<ReservationTime> findByStartAt(final LocalTime startAt) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = :startAt";
        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("startAt", startAt);
            ReservationTime reservationTime = template.queryForObject(sql, param, itemRowMapper());

            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

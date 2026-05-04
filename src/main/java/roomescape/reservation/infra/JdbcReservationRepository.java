package roomescape.reservation.infra;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.time.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        ReservationTime time = ReservationTime.builder()
                .id(resultSet.getLong("t_id"))
                .startAt(resultSet.getTime("start_at").toLocalTime())
                .build();

        return Reservation.builder()
                .id(resultSet.getLong("r_id"))
                .name(resultSet.getString("name"))
                .date(resultSet.getDate("date").toLocalDate())
                .time(time)
                .build();
    };


    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id) "
                + "VALUES(:name, :date, :timeId)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder);
        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        reservation.setId(generatedId);
        return reservation;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS r_id, r.name, r.date, " +
                "t.id AS t_id, t.start_at " +
                "FROM reservation r " +
                "JOIN reservation_time t ON r.time_id = t.id " +
                "ORDER BY r.date ASC, t.start_at ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean existsByReservationTime(Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id=:timeId)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("timeId", timeId), Boolean.class));
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id=:id)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("id", id), Boolean.class));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id=:id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}

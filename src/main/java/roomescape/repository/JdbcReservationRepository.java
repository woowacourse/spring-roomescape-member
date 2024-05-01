package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        LocalDate date = rs.getObject("date", LocalDate.class);

        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("time_start_at", LocalTime.class)
        );

        return new Reservation(id, name, date, time);
    };

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                    SELECT
                        r.id as reservation_id,
                        r.name,
                        r.date,
                        t.id as time_id,
                        t.start_at as time_start_at
                    FROM reservation as r
                    join reservation_time as t
                    on r.time_id = t.id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";

        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Reservation save(Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByTimeId(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
    }

    @Override
    public boolean existsByThemeId(Long id) {
            String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";

            return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }
}

package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationDao implements ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query("""
                        SELECT r.id as reservation_id, r.name, r.date,
                            t.id as time_id, t.start_at as time_value
                        FROM reservation as r
                        inner join reservation_time as t
                        on r.time_id = t.id""",
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        new Name(rs.getString("name")),
                        rs.getDate("date").toLocalDate(),
                        new ReservationTime(rs.getLong("time_id"),
                                rs.getTime("time_value").toLocalTime())
                ));
    }

    @Override
    public boolean existsByDateTime(LocalDate date, Long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT * FROM reservation WHERE date = ? AND time_id = ?)",
                Boolean.class, date, timeId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return reservation.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) > 0;
    }
}

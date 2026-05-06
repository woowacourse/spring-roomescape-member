package roomescape.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTime;

@Repository
public class ReservationJdbcTemplateRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.time().id()
        );
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Reservation.createWithId(
                id,
                reservation.name(),
                reservation.date(),
                reservation.time()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
        SELECT r.id, r.name, r.date,
               rt.id AS time_id, rt.start_at
        FROM reservation r
        JOIN reservation_time rt ON r.time_id = rt.id
        WHERE r.id = ?
        """;
        List<Reservation> reservation = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    ReservationTime time = ReservationTime.createWithId(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    );

                    return Reservation.createWithId(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getDate("date").toLocalDate(),
                            time
                    );
                },
                id
        );

        return reservation.stream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
        SELECT r.id, r.name, r.date,
               rt.id as time_id, rt.start_at
        FROM reservation r
        JOIN reservation_time rt ON r.time_id = rt.id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationTime time = ReservationTime.createWithId(
                    rs.getLong("time_id"),
                    rs.getTime("start_at").toLocalTime()
            );

            return Reservation.createWithId(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    time
            );
        });
    }

    @Override
    public boolean existsByReservationTimeId(Long id) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM reservation
                WHERE time_id = ?
            );
        """;
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result != null && result == 1;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}

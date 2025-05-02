package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> mapper;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insertActor;

    static {
        mapper = (resultSet, resultNumber) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }

    public H2ReservationRepository(DataSource dataSource, JdbcTemplate template) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.template = template;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id as reservation_id, r.name, r.date, 
                       rt.id as time_id, rt.start_at, 
                       t.id as theme_id, t.name as theme_name, t.description, t.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON r.theme_id = t.id 
                """;
        return template.query(sql, mapper);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = """
                SELECT r.id as reservation_id, r.name, r.date,
                       rt.id as time_id, rt.start_at,
                       t.id as theme_id, t.name as theme_name, t.description, t.thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id 
                INNER JOIN theme AS t ON r.theme_id = t.id 
                WHERE r.id = ?
                """;
        try {
            Reservation reservation = template.queryForObject(sql, mapper, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT * 
                    FROM reservation AS r
                    INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                    WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public boolean checkExistenceInTime(long reservationTimeId) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT * 
                    FROM reservation AS r 
                    INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                    WHERE r.time_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    @Override
    public boolean checkExistenceInTheme(long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT * 
                    FROM reservation AS r 
                    INNER JOIN theme AS t ON r.theme_id = t.id 
                    WHERE r.theme_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public long add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE reservation.id = ?";
        template.update(sql, id);
    }
}

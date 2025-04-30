package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {

    private static final RowMapper<Reservation> mapper;
    private final JdbcTemplate template;

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

    public ReservationRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name, r.date, "
                + "rt.id as time_id, rt.start_at, "
                + "t.id as theme_id, t.name as theme_name, t.description, t.thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id ";
        return template.query(sql, mapper);
    }

    public Optional<Reservation> findById(long id) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name, r.date, "
                + "rt.id as time_id, rt.start_at, "
                + "t.id as theme_id, t.name as theme_name, t.description, t.thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id "
                + "WHERE r.id = ?";
        try {
            Reservation reservation = template.queryForObject(sql, mapper, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean checkExistenceByDateTime(LocalDate date, long timeId) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "WHERE r.date = ? AND r.time_id = ?)";
        return template.queryForObject(sql, Boolean.class, date, timeId);
    }

    public boolean checkExistenceInTime(long reservationTimeId) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "WHERE r.time_id = ?)";
        return template.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    public long add(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                (connection) -> {
                    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, reservation.getName());
                    statement.setDate(2, Date.valueOf(reservation.getDate()));
                    statement.setLong(3, reservation.getTime().getId());
                    statement.setLong(4, reservation.getTheme().getId());
                    return statement;
                },
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE reservation.id = ?";
        template.update(sql, id);
    }
}

package roomescape.reservation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class ReservationDao implements Dao<Reservation> {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Reservation add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value, "
                + "e.id AS theme_id, "
                + "e.name AS theme_name, "
                + "e.description AS theme_description, "
                + "e.thumbnail AS theme_thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN theme AS e "
                + "ON r.theme_id = e.id "
                + "WHERE r.id = :id";
        Map<String, Object> parameter = Map.of("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createReservation(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value, "
                + "e.id AS theme_id, "
                + "e.name AS theme_name, "
                + "e.description AS theme_description, "
                + "e.thumbnail AS theme_thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id "
                + "INNER JOIN theme AS e "
                + "ON r.theme_id = e.id ";

        return namedParameterJdbcTemplate.query(sql, (resultSet, rowNum) -> createReservation(resultSet));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Reservation createReservation(ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                ));
    }
}

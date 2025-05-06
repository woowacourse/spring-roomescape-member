package roomescape.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
               select r.id as reservation_id, 
                      r.name, 
                      r.date,
            
                      rt.id as time_id,
                      rt.start_at as time_value,
            
                      t.id as theme_id,
                      t.name as theme_name,
                      t.description as theme_description,
                      t.thumbnail as theme_thumbnail
               from reservation as r 
               inner join reservation_time as rt on r.time_id = rt.id
               inner join theme as t on r.theme_id = t.id 
            """;

        return jdbcTemplate.query(sql,
            (resultSet, RowNum) ->
                new Reservation(resultSet.getLong("reservation_id"),
                    createPerson(resultSet),
                    createReservationDate(resultSet),
                    createReservationTime(resultSet),
                    createTheme(resultSet)
                ));
    }

    private ReservationDate createReservationDate(ResultSet resultSet) throws SQLException {
        return new ReservationDate(LocalDate.parse(resultSet.getString("date")));
    }

    private Person createPerson(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getString("name"));
    }

    private Theme createTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
            resultSet.getLong("theme_id"),
            resultSet.getString("theme_name"),
            resultSet.getString("theme_description"),
            resultSet.getString("theme_thumbnail")
        );
    }

    private ReservationTime createReservationTime(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
            resultSet.getLong("time_id"),
            LocalTime.parse(resultSet.getString("time_value")));
    }

    @Override
    public long save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", reservation.getPersonName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public void delete(Long id) {
        String query = "delete from reservation where id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String query = """
                SELECT r.id AS reservation_id,
                       r.name,
                       r.date,
            
                       rt.id AS time_id,
                       rt.start_at AS time_value,
            
                       t.id AS theme_id,
                       t.name AS theme_name,
                       t.description AS theme_description,
                       t.thumbnail AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                WHERE r.id = ?
            """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) ->
                    new Reservation(
                        resultSet.getLong("id"),
                        createPerson(resultSet),
                        createReservationDate(resultSet),
                        createReservationTime(resultSet),
                        createTheme(resultSet)
                    ),
                id
            );
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int countExistReservationByTime(Long id) {
        String query = "select count(*) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }

    @Override
    public int countExistReservationByTheme(Long id) {
        String query = "select count(*) from reservation where theme_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }

    @Override
    public int countAlreadyReservationOf(ReservationDate date, Long timeId) {
        String query = "select count(*) from reservation where date = ? AND time_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, date.getDate(), timeId);
    }
}

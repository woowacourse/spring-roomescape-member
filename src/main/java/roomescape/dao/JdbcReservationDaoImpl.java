package roomescape.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<Reservation> findAllReservation() {
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
    public void saveReservation(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", reservation.getPersonName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        Number newId = insertActor.executeAndReturnKey(parameters);
        reservation.setId(newId.longValue());
    }

    @Override
    public void deleteReservation(Long id) {
        String query = "delete from reservation where id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public int findByTimeId(Long id) {
        String query = "select count(*) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }

    @Override
    public int findByDateAndTime(ReservationDate date, Long timeId) {
        String query = "select count(*) from reservation where date = ? AND time_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, date.getDate(), timeId);
    }

    @Override
    public int findAlreadyExistReservationBy(String date, long timeId, Long themeId) {
        String query = "select count(*) from reservation where date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, date, timeId, themeId);
    }
}

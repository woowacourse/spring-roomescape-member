package roomescape.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String findAllSql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "FROM reservation as r inner join reservation_time as t on r.time_id = t.id";
        return jdbcTemplate.query(findAllSql,
                (resultSet, numRow) -> new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                resultSet.getString("time_value")
                        )
                )
        );
    }

    public Long insert(String name, String date, Long timeId) {
        String insertSql = "INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String deleteFromIdSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteFromIdSql, id);
    }

    public Optional<Reservation> findById(Long id) {
        String findByIdSql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "FROM reservation as r inner join reservation_time as t on r.time_id = t.id WHERE r.id = ?";
        List<Reservation> reservations = jdbcTemplate.query(findByIdSql,
                (resultSet, numRow) -> new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                resultSet.getString("time_value")
                        )
                ), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(reservations));
    }

    public int countByTimeId(Long timeId) {
        String findByIdSql = "SELECT count(*) FROM reservation WHERE time_id =?";
        return jdbcTemplate.queryForObject(findByIdSql, Integer.class, timeId);
    }
}

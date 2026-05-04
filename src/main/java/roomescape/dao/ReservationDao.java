package roomescape.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getString("start_at")
        );

        Reservation reservation = new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                reservationTime
        );
        return reservation;
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation insert(Reservation reservation) {
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(
                        sql, new String[]{"id"}
                );
                statement.setString(1, reservation.getName());
                statement.setString(2, reservation.getDate());
                statement.setLong(3, reservation.getTime().getId());

                return statement;
            }
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();
        return new Reservation(generatedId, reservation.getName(), reservation.getDate(), new ReservationTime(reservation.getTime().getId(), reservation.getTime().getStartAt()));
    }

    public List<Reservation> select() {
        String sql = "select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as start_at from reservation as r "
                + "inner join reservation_time as t on r.time_id = t.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}

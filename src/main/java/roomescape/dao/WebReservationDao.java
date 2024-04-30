package roomescape.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public class  WebReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public WebReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> readAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.`date`,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM reservation r
                    INNER JOIN reservation_time t
                    ON r.time_id = t.id;
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> getReservation(resultSet, getReservationTime(resultSet))
        );
    }

    @Override
    public Reservation create(Reservation reservation) {
        String sql = """
                INSERT
                INTO reservation
                    (name, date, time_id)
                VALUES
                    (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> getPreparedStatement(reservation, connection, sql),
                keyHolder
        );
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime()
        );
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE
                FROM reservation
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    private Reservation getReservation(ResultSet resultSet, ReservationTime reservationTime) throws SQLException {
        return new Reservation(
                resultSet.getLong("id"),
                new ReservationName(resultSet.getString("name")),
                ReservationDate.from(resultSet.getString("date")),
                reservationTime
        );
    }

    private ReservationTime getReservationTime(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("time_id"),
                ReservationStartAt.from(resultSet.getString("time_value"))
        );
    }

    private PreparedStatement getPreparedStatement(Reservation reservation,
                                                   Connection connection,
                                                   String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
        preparedStatement.setString(1, reservation.getName().getValue());
        preparedStatement.setString(2, reservation.getDate().toStringDate());
        preparedStatement.setLong(3, reservation.getReservationTime().getId());
        return preparedStatement;
    }
}

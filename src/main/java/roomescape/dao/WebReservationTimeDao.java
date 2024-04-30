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
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public class WebReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public WebReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReservationTime> readAll() {
        String sql = """
                SELECT
                id, start_at
                FROM reservation_time
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> getReservationTime(resultSet)
        );
    }

    @Override
    public ReservationTime readById(long id) {
        String sql = """
                SELECT
                id, start_at
                FROM reservation_time
                WHERE id = ?
                """;
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> getReservationTime(resultSet),
                id
        );
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        String sql = """
                INSERT
                INTO reservation_time
                    (start_at)
                VALUES
                    (?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> getPreparedStatement(reservationTime, connection, sql),
                keyHolder
        );
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation_time WHERE id = ?)
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
                FROM reservation_time
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    private ReservationTime getReservationTime(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("id"),
                ReservationStartAt.from(resultSet.getString("start_at"))
        );
    }

    private PreparedStatement getPreparedStatement(ReservationTime reservationTime,
                                                   Connection connection,
                                                   String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
        preparedStatement.setString(1, reservationTime.getStartAt().toStringTime());
        return preparedStatement;
    }
}

package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation save(final Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into reservation (name, date, time_id) values (?, ?, ?)",
                            new String[]{"id"}
                    );
                    ps.setString(1, reservation.getName());
                    ps.setString(2, reservation.getDate().toString());
                    ps.setString(3, String.valueOf(reservation.getTimeId()));
                    return ps;
                }, keyHolder
        );

        try {
            long id = keyHolder.getKey().longValue();
            return new Reservation(
                    id,
                    reservation.name(),
                    reservation.date(),
                    reservation.time()
            );
        } catch (NullPointerException exception) {
            throw new RuntimeException("[ERROR] 예약 요청이 정상적으로 이루어지지 않았습니다.");
        }
    }

    public Optional<Reservation> findById(long id) {
        try {
            String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value "
                    + "FROM reservation as r "
                    + "INNER JOIN reservation_time as t "
                    + "ON r.time_id = t.id "
                    + "WHERE r.id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            Reservation.of(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("date"),
                    resultSet.getLong("time_id"),
                    resultSet.getString("start_at")
            );

    public List<Reservation> getAll() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Reservation.of(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at")
                )
        );
    }

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", Long.valueOf(id));
    }
}

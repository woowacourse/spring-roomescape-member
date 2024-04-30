package roomescape.repository;

import java.sql.PreparedStatement;
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
                            "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                            new String[]{"id"}
                    );
                    ps.setString(1, reservation.getName().getValue());
                    ps.setString(2, reservation.getDate().toString());
                    ps.setString(3, String.valueOf(reservation.getTimeId()));
                    ps.setString(4, String.valueOf(reservation.getThemeId()));
                    return ps;
                }, keyHolder
        );

        try {
            long id = keyHolder.getKey().longValue();
            return new Reservation(
                    id,
                    reservation.getName(),
                    reservation.getDate(),
                    reservation.getTime(),
                    reservation.getTheme()
            );
        } catch (NullPointerException exception) {
            throw new RuntimeException("[ERROR] 예약 요청이 정상적으로 이루어지지 않았습니다.");
        }
    }

    public Optional<Reservation> findById(long id) {
        try {
            String sql =
                    "SELECT r.id as reservation_id, r.name, r.date, time.id as time_id, time.start_at as time_value, "
                            + "theme.id as theme_id, theme.name as theme_name, theme.description, theme.thumbnail "
                            + "FROM reservation as r "
                            + "INNER JOIN reservation_time as time "
                            + "ON r.time_id = time.id "
                            + "INNER JOIN theme as theme "
                            + "ON r.theme_id = theme.id "
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
                    resultSet.getString("start_at"),
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    public List<Reservation> getAll() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, time.id as time_id, time.start_at as time_value, "
                + "theme.id as theme_id, theme.name as theme_name, theme.description, theme.thumbnail "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as time "
                + "ON r.time_id = time.id "
                + "INNER JOIN theme as theme "
                + "ON r.theme_id = theme.id ";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Reservation.of(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at"),
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", Long.valueOf(id));
    }

    public List<Reservation> findByTimeId(final long timeId) {
        String sql = "SELECT * FROM reservation WHERE time_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Reservation.of(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getLong("time_id"),
                        resultSet.getLong("theme_id")
                ),
                timeId);
    }

    public List<Reservation> findByDateAndTimeId(final LocalDate date, final long timeId) {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, time.id as time_id, time.start_at as time_value, "
                + "theme.id as theme_id, theme.name as theme_name, theme.description, theme.thumbnail "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as time "
                + "ON r.time_id = time.id "
                + "INNER JOIN theme as theme "
                + "ON r.theme_id = theme.id "
                + "WHERE r.date = ? "
                + "AND r.time_id = ?";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Reservation.of(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("date"),
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at"),
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ),
                date,
                timeId);
    }
}

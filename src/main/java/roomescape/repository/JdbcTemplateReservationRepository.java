package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("reservation_name"),
            rs.getDate("reservation_date").toLocalDate(),
            new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()
            ),
            new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            )
    );

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(reservation, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation);
    }

    private void save(Reservation reservation, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            String sql = "INSERT INTO RESERVATION(name,date,time_id,THEME_ID) VALUES ( ?,?,?,? )";
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getReservationTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public Reservations findAll() {
        String query = """
                SELECT
                    R.id AS reservation_id,
                    R.name AS reservation_name,
                    R.date AS reservation_date,
                    T.id AS time_id,
                    T.start_at AS time_value,
                    T2.id AS theme_id,
                    T2.name AS theme_name,
                    T2.description AS description,
                    T2.thumbnail AS thumbnail
                FROM RESERVATION AS R
                    INNER JOIN RESERVATION_TIME T ON R.time_id = T.id
                    INNER JOIN THEME T2 ON T2.id = R.theme_id""";

        List<Reservation> findReservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER);
        return new Reservations(findReservations);
    }

    @Override
    public Reservations findByThemeAndDate(Theme theme, LocalDate date) {
        String query = """
                SELECT
                    R.id AS reservation_id,
                    R.name AS reservation_name,
                    R.date AS reservation_date,
                    T.id AS time_id,
                    T.start_at AS time_value,
                    T2.id AS theme_id,
                    T2.name AS theme_name,
                    T2.description AS description,
                    T2.thumbnail AS thumbnail
                 FROM RESERVATION AS R
                     INNER JOIN RESERVATION_TIME T ON R.time_id = T.id
                     INNER JOIN THEME T2 ON T2.id = R.theme_id
                 WHERE theme_id = ? AND R.date = ?;
                 """;

        List<Reservation> findReservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER, theme.getId(),
                Date.valueOf(date));
        return new Reservations(findReservations);
    }

    @Override
    public boolean existByTimeId(long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT time_id FROM RESERVATION WHERE time_id = ?)",
                Boolean.class, timeId
        );
    }

    @Override
    public boolean existByThemeId(long themeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT theme_id FROM RESERVATION WHERE theme_id = ?)",
                Boolean.class, themeId
        );
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE id = ?", id);
    }
}

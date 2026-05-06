package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.InfrastructureException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcReservationRepository implements ReservationRepository {


    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query("""
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                """, reservationRowMapper);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.query("""
                SELECT 
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail 
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                WHERE date = ? AND theme_Id = ?
                """, reservationRowMapper, date, themeId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = insert(reservation, keyHolder);
        validateCreatedRowCount(rowCount);

        Long id = getGeneratedId(keyHolder);
        return reservation.withId(id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("""
                DELETE FROM reservation
                WHERE id = ?
                """, id);
    }

    private int insert(Reservation reservation, KeyHolder keyHolder) {
        return jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            INSERT INTO reservation (name, date, time_id, theme_id)
                            VALUES (?, ?, ?, ?)
                            """,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());
            return preparedStatement;
        }, keyHolder);
    }

    private void validateCreatedRowCount(int rowCount) {
        if (rowCount != 1) {
            throw new InfrastructureException(ErrorCode.RESERVATION_CREATE_FAILED);
        }
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new InfrastructureException(ErrorCode.RESERVATION_CREATE_FAILED);
        }
        return key.longValue();
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                reservationTime,
                theme
        );
    };
}

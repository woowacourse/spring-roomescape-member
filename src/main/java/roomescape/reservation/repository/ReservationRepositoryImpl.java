package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.globalexception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id,\n" +
            "       r.name,\n" +
            "       r.date,\n" +
            "       time.id AS time_id,\n" +
            "       time.start_at AS time_value,\n" +
            "       theme.id AS theme_id,\n" +
            "       theme.name AS theme_name,\n" +
            "       theme.description AS theme_description,\n" +
            "       theme.thumbnail AS theme_thumbnail\n" +
            "FROM reservation AS r INNER JOIN reservation_time AS time\n" +
            "    ON r.time_id = time.id\n" +
            "    INNER JOIN theme AS theme\n" +
            "    ON r.theme_id = theme.id";
        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> {
                ReservationTime time = new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
                );

                Theme theme = new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
                );

                Reservation reservation = Reservation.withId(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    time,
                    theme
                );
                return reservation;
            });
    }

    @Override
    public Reservation findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException("해당 예약 id가 존재하지 않습니다."));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id AS reservation_id,\n" +
            "       r.name,\n" +
            "       r.date,\n" +
            "       time.id AS time_id,\n" +
            "       time.start_at AS time_value,\n" +
            "       theme.id AS theme_id,\n" +
            "       theme.name AS theme_name,\n" +
            "       theme.description AS theme_description,\n" +
            "       theme.thumbnail AS theme_thumbnail\n" +
            "FROM reservation AS r\n" +
            "    INNER JOIN reservation_time AS time\n" +
            "    ON r.time_id = time.id\n" +
            "    INNER JOIN theme AS theme\n" +
            "    ON r.theme_id = theme.id\n" +
            "WHERE r.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> {
                    ReservationTime time = new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                    );

                    Theme theme = new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                    );

                    return Reservation.withId(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("date").toLocalDate(),
                        time,
                        theme
                    );
                }, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByThemeAndDate(Theme theme, LocalDate date) {
        String sql = "SELECT r.id AS id, r.name AS name, r.date AS date, t.id AS time_id, t.start_at AS time_value "
            + "FROM reservation AS r INNER JOIN reservation_time AS t "
            + "ON r.time_id = t.id "
            + "WHERE r.date = ? AND r.theme_id = ?";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_value").toLocalTime()
            );

            return Reservation.withId(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                time,
                theme
            );
        }, date, theme.getId());
    }

    @Override
    public Reservation add(Reservation reservation) {
        Long id = insertWithKeyHolder(reservation);
        return findByIdOrThrow(id);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByReservationTime(ReservationTime reservationTime) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, reservationTime.getId()) >= 1;
    }

    private Long insertWithKeyHolder(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Long reservationTimeId = reservation.getReservationTime().getId();
        Long themeId = reservation.getTheme().getId();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                sql,
                new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservationTimeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean existsByTheme(Theme theme) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, theme.getId()) >= 1;
    }
}

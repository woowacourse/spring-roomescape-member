package roomescape.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String RESERVATION_BASE_SELECT = """
                 SELECT r.id,
                       r.name AS reservation_name,
                       r.date,
                       rt.id AS time_id,
                       rt.start_at,
                       t.id AS theme_id,
                       t.name AS theme_name,
                       t.description,
                       t.thumbnail_url
                FROM reservation AS r
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON rt.theme_id = t.id
            """;

    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        Theme theme = Theme.of(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_url")
        );

        ReservationTime reservationTime = ReservationTime.of(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime(),
                theme
        );

        return Reservation.of(
                resultSet.getLong("id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("date").toLocalDate(),
                reservationTime
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = RESERVATION_BASE_SELECT;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public List<Long> findAllByDateAndThemeId(final LocalDate date, final long themeId) {
        final String sql = """
                SELECT rt.id AS time_id
                FROM reservation AS r
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                WHERE rt.theme_id = ? AND r.date = ?
                """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                themeId,
                Date.valueOf(date)
        );
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        String sql = RESERVATION_BASE_SELECT + "WHERE r.id = ?";

        return jdbcTemplate.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void deleteById(final long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getTime().getId());
            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("[ERROR] 예약 ID를 생성하지 못했습니다.");
        }

        return reservation.withId(key.longValue());
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                timeId
        ));
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                Date.valueOf(date),
                timeId
        ));
    }


}

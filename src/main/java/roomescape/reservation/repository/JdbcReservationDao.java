package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper =
            (rs, rowNum) -> {
                ReservationTime time = new ReservationTime(
                        rs.getLong("time_id"),
                        LocalTime.parse(rs.getString("time_start_at"))
                );

                Theme theme = new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_des"),
                        rs.getString("theme_thumb")
                );

                return new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("reservation_name"),
                        LocalDate.parse(rs.getString("reservation_date")),
                        time,
                        theme
                );
            };

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, reservation.getName());
                    preparedStatement.setString(2, reservation.getDate().toString());
                    preparedStatement.setLong(3, reservation.getTime().getId());
                    preparedStatement.setLong(4, reservation.getTheme().getId());
                    return preparedStatement;
                },
                keyHolder
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                WHERE r.id = ?
                """;

        List<Reservation> findReservations = jdbcTemplate.query(sql, rowMapper, reservationId);

        if (findReservations.isEmpty()) {
            return Optional.empty();
        }
        if (findReservations.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }
        return Optional.of(findReservations.getFirst());
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, timeId);
    }

    @Override
    public List<Reservation> findAllByThemeId(Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                WHERE r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, themeId);
    }
}

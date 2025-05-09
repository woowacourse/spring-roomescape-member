package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.User;

@Component
@RequiredArgsConstructor
public class ReservationDao {

    private static final RowMapper<Reservation> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            new User(
                    resultSet.getLong("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_email"),
                    resultSet.getString("user_password"),
                    resultSet.getString("user_role")
            ),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new ReservationTheme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("th_name"),
                    resultSet.getString("th_description"),
                    resultSet.getString("th_thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;

    public List<Reservation> selectAll() {
        String selectAllQuery = """
                SELECT r.id, r.date, r.time_id, r.theme_id,
                        u.id AS user_id, u.name AS user_name, u.email AS user_email, u.password AS user_password, u.role AS user_role,
                        rt.start_at, th.name AS th_name,
                        th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN users u ON r.user_id = u.id
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(selectAllQuery, DEFAULT_ROW_MAPPER);
    }

    public Reservation insertAndGet(Reservation reservation) {
        String insertQuery = "INSERT INTO reservation (user_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setLong(1, reservation.user().id());
            ps.setString(2, reservation.date().toString());
            ps.setLong(3, reservation.time().id());
            ps.setLong(4, reservation.theme().id());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        return reservation.withId(id);
    }

    public Optional<Reservation> selectById(Long id) {
        String selectQuery = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id,
                        u.id AS user_id, u.name AS user_name, u.email AS user_email, u.password AS user_password,
                        rt.start_at, th.name AS th_name,
                        th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN users u ON r.user_id = u.id
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteQuery, id);
    }

    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        String query = """
                SELECT EXISTS (
                        SELECT 1
                        FROM reservation
                        WHERE time_id = ? AND date = ? AND theme_id = ?)
                """;
        return Optional.ofNullable(
                        jdbcTemplate.queryForObject(query, Boolean.class, timeId, date, themeId))
                .orElse(false);
    }
}

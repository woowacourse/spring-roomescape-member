package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.*;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            )
    );

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date,
                    u.id AS member_id,
                    u.name AS member_name,
                    u.email AS email,
                    u.password AS password,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM
                    reservation AS r
                INNER JOIN member AS u ON r.member_id = u.id
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date,
                    u.id AS member_id,
                    u.name AS member_name,
                    u.email AS email,
                    u.password AS password,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM
                    reservation AS r
                INNER JOIN member AS u ON r.member_id = u.id
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date = ? AND r.theme_id = ?;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, date, themeId);
    }

    public Reservation save(Reservation reservation) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("date", reservation.getDate())
                    .addValue("member_id", reservation.getMember().getId())
                    .addValue("time_id", reservation.getReservationTime().getId())
                    .addValue("theme_id", reservation.getTheme().getId());
            Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
            return new Reservation(
                    id,
                    reservation.getMember(),
                    reservation.getDate(),
                    reservation.getReservationTime(),
                    reservation.getTheme()
            );
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

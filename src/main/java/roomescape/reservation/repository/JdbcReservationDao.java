package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private static final String FIND_ALL_SQL =
            """
                        SELECT
                            r.id AS reservation_id, r.date AS reservation_date,
                            t.id AS time_id, t.start_at AS time_start_at,
                            th.id AS theme_id, th.name AS theme_name, th.description AS theme_des, th.thumbnail AS theme_thumb,
                            m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role AS member_role
                        FROM reservation AS r
                        INNER JOIN reservation_time AS t ON r.time_id = t.id
                        INNER JOIN theme AS th ON r.theme_id = th.id
                        INNER JOIN member as m ON r.member_id = m.id
                    """;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper =
            (rs, rowNum) -> new Reservation(
                    rs.getLong("reservation_id"),
                    LocalDate.parse(rs.getString("reservation_date")),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            LocalTime.parse(rs.getString("time_start_at"))
                    ),
                    new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("theme_des"),
                            rs.getString("theme_thumb")
                    ),
                    new Member(
                            rs.getLong("member_id"),
                            rs.getString("member_name"),
                            rs.getString("member_email"),
                            rs.getString("member_password"),
                            Role.valueOf(rs.getString("member_role"))
                    )
            );

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        String sql = "INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, reservation.getDate().toString());
                    preparedStatement.setLong(2, reservation.getTimeId());
                    preparedStatement.setLong(3, reservation.getThemeId());
                    preparedStatement.setLong(4, reservation.getMemberId());
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
        String sql = FIND_ALL_SQL + " WHERE r.id = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, reservationId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, rowMapper);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        String sql = FIND_ALL_SQL + " WHERE r.time_id = ?";
        return jdbcTemplate.query(sql, rowMapper, timeId);
    }

    @Override
    public List<Reservation> findAllByThemeId(Long themeId) {
        String sql = FIND_ALL_SQL + " WHERE r.theme_id = ?";
        return jdbcTemplate.query(sql, rowMapper, themeId);
    }

}

package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper =
            (rs, rowNum) -> {
                ReservationTime time = new ReservationTime(
                        rs.getLong("time_id"),
                        LocalTime.parse(rs.getString("time_value"))
                );

                Theme theme = new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_des"),
                        rs.getString("theme_thumb")
                );

                Member member = new Member(
                        rs.getLong("member_id"),
                        rs.getString("member_email"),
                        rs.getString("member_password"),
                        rs.getString("member_name"),
                        rs.getString("member_role")
                );

                return new Reservation(
                        rs.getLong("reservation_id"),
                        member,
                        LocalDate.parse(rs.getString("date")),
                        time,
                        theme
                );
            };

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        String sql = "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setLong(1, reservation.getMember().getId());
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
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb,
                    m.id AS member_id,
                    m.email AS member_email,
                    m.password AS member_password,
                    m.name AS member_name,
                    m.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m
                ON r.member_id = m.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Boolean existReservationByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public Boolean existReservationByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public Boolean existReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation AS r
                    INNER JOIN reservation_time AS t
                    ON r.time_id = t.id
                    INNER JOIN theme AS th  
                    ON r.theme_id = th.id
                    WHERE r.date = ? AND t.id = ? AND th.id = ?
                );                  
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb,
                    m.id AS member_id,
                    m.email AS member_email,
                    m.password AS member_password,
                    m.name AS member_name,
                    m.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m
                ON r.member_id = m.id
                WHERE r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    @Override
    public List<Reservation> findAllByThemeIdAndMemberIdAndPeriod(Long themeId, Long memberId, LocalDate dateFrom,
                                                                  LocalDate dateTo) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb,
                    m.id AS member_id,
                    m.email AS member_email,
                    m.password AS member_password,
                    m.name AS member_name,
                    m.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m
                ON r.member_id = m.id
                WHERE r.theme_id = ? AND r.member_id = ? AND (r.date BETWEEN ? AND ?)
                """;
        return jdbcTemplate.query(sql, rowMapper, themeId, memberId, dateFrom, dateTo);
    }
}

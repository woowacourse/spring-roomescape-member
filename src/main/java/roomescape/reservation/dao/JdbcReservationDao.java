package roomescape.reservation.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.service.ReservationDao;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private static final RowMapper<Reservation> RESERVATION_MAPPER = (resultSet, row) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password"),
                            Enum.valueOf(MemberRole.class, resultSet.getString("member_role"))),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    ),
                    resultSet.getTimestamp("created_at").toLocalDateTime()
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservation);
        Number id = jdbcInsert.executeAndReturnKey(parameterSource);
        return new Reservation(id.longValue(), reservation.getMember(), reservation.getDate(), reservation.getTime(), reservation.getTheme(), reservation.getCreatedAt());
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """       
                SELECT
                    r.id as reservation_id,
                    r.date,
                    r.created_at,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    m.role as member_role,
                    t.id as time_id,
                    t.start_at as time_value,
                    h.id as theme_id,
                    h.name as theme_name,
                    h.description as theme_description,
                    h.thumbnail as theme_thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as h on r.theme_id = h.id
                inner join member as m on r.member_id = m.id
                """;

        return jdbcTemplate.query(sql, RESERVATION_MAPPER);
    }

    @Override
    public List<Reservation> findReservationsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    r.created_at,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    m.role as member_role,
                    t.id as time_id,
                    t.start_at as time_value,
                    h.id as theme_id,
                    h.name as theme_name,
                    h.description as theme_description,
                    h.thumbnail as theme_thumbnail 
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as h on r.theme_id = h.id
                inner join member as m on r.member_id = m.id
                WHERE r.date = ? and r.theme_id = ?
                """;

        return jdbcTemplate.query(sql, RESERVATION_MAPPER, date, themeId);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}

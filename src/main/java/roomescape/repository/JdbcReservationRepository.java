package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.user.Email;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.model.user.Password;
import roomescape.model.user.Role;

@Repository
public class JdbcReservationRepository implements ReservationRepository, ReservedTimeChecker, ReservedThemeChecker,
        ReservedChecker {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static RowMapper<Reservation> getReservationRowMapper() {
        return (rs, rowNum) -> {
            ReservationDateTime dateTime = new ReservationDateTime(
                    LocalDate.parse(rs.getString("date")),
                    new ReservationTime(rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()));

            Theme theme = new Theme(rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("description"),
                    rs.getString("thumbnail"));

            Member member = new Member(rs.getLong("member_id"),
                    new Name(rs.getString("member_name")),
                    new Email(rs.getString("email")),
                    new Password(rs.getString("password")),
                    Role.of(rs.getString("role")));

            return new Reservation(rs.getLong("id"), member, dateTime, theme);
        };
    }

    @Override
    public List<Reservation> getReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                                SELECT r.id, r.member_id, r.date,
                                       r.time_id, t.start_at, r.theme_id,
                                       th.name AS theme_name, th.description, th.thumbnail,
                                       m.name AS member_name, m.email, m.password, m.role
                                FROM reservation as r inner join reservation_time as t on r.time_id = t.id
                                inner join theme as th on r.theme_id = th.id
                                inner join member as m on r.member_id = m.id
                                where true;
                """;

        List<Object> params = new ArrayList<>();

        if (themeId != null) {
            sql += " and r.theme_id = ?";
            params.add(themeId);
        }
        if (memberId != null) {
            sql += " and r.member_id = ?";
            params.add(memberId);
        }
        if (dateFrom != null) {
            sql += " and r.date >= ?";
            params.add(dateFrom);
        }
        if (dateTo != null) {
            sql += " and r.date <= ?";
            params.add(dateTo);
        }

        if (params.isEmpty()) {
            return jdbcTemplate.query(sql, getReservationRowMapper());
        }

        return jdbcTemplate.query(sql, getReservationRowMapper(), params.toArray());
    }

    @Override
    public boolean contains(LocalDate reservationDate, Long timeId, Long themeId) {
        String sql = "select exists (select 1 from reservation where date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationDate, timeId, themeId);
    }

    @Override
    public Reservation addReservation(Reservation reservationWithNoId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, new String[]{"id"});
            ps.setLong(1, reservationWithNoId.getMember().getId());
            ps.setString(2, reservationWithNoId.getReservationDateTime().getDate().toString());
            ps.setLong(3, reservationWithNoId.getReservationDateTime().getTime().getId());
            ps.setLong(4, reservationWithNoId.getTheme().getId());
            return ps;
        }, keyHolder);
        return new Reservation(Objects.requireNonNull(keyHolder.getKey()).longValue(),
                reservationWithNoId.getMember(),
                reservationWithNoId.getReservationDateTime(), reservationWithNoId.getTheme());
    }

    @Override
    public void deleteReservation(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    @Override
    public boolean isReservedTime(Long timeId) {
        String sql = "select exists (select 1 from reservation where time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean isReservedTheme(Long themeId) {
        String sql = "select exists (select 1 from reservation where theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public List<Long> getBestThemesIdInDays(LocalDate startDate, LocalDate endDate) {
        String sql = "select theme_id from reservation where date between ? and ? group by theme_id order by count(*) desc limit 10";

        List<Long> longs = jdbcTemplate.queryForList(
                sql,
                Long.class,
                startDate.toString(),
                endDate.toString()
        );
        return longs;
    }
}

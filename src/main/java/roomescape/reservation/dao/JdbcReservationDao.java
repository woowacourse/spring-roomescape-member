package roomescape.reservation.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.member.dao.MemberDao;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final MemberDao memberDao;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate, MemberDao memberDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberDao = memberDao;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                    SELECT 
                        r.id as reservation_id, 
                        r.date, 
                        rt.id as time_id, 
                        rt.start_at as time_value, 
                        t.id as theme_id,
                        t.name as theme_name,
                        t.description as theme_des,
                        t.thumbnail as theme_thumb,
                        m.id as member_id,
                        m.name as member_name,
                        m.email as member_email,
                        m.password as member_password
                    FROM reservation as r 
                    inner join reservation_time as rt on r.time_id = rt.id
                    inner join theme as t on t.id = r.theme_id
                    INNER JOIN member as m ON m.id = r.member_id
                """;

        return this.jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    ReservationTime reservationTime = new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getObject("time_value", LocalTime.class)
                    );

                    Theme theme = Theme.of(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_des"),
                            resultSet.getString("theme_thumb")
                    );

                    Member member = new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password")
                    );
                    return Reservation.of(
                            resultSet.getLong("reservation_id"),
                            member,
                            resultSet.getObject("date", LocalDate.class),
                            reservationTime,
                            theme
                    );
                });
    }

    @Override
    public Long create(Reservation reservation) {
        String sql = "insert into reservation (date, member_id, time_id, theme_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, reservation.getDate().toString());
            ps.setLong(2, reservation.getMember().getId());
            ps.setLong(3, reservation.getReservationTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findByTimeId(Long id) {
        String sql = """
                SELECT r.id as reservation_id,
                       r.member_id as member_id,
                       r.date as reservation_date,
                       rt.start_at as time_start_at,
                       rt.id as time_id,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description as theme_des,
                       t.thumbnail as theme_thumb
                    FROM reservation as r 
                    INNER JOIN reservation_time as rt ON rt.id = r.time_id 
                    INNER JOIN theme as t ON r.theme_id = t.id
                    WHERE r.time_id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = Theme.of(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        Long memberId = rs.getLong("member_id");
                        Member member = memberDao.findById(memberId)
                                .orElseThrow(() -> new NoSuchElementException());

                        return Reservation.of(
                                rs.getLong("reservation_id"),
                                member,
                                rs.getDate("reservation_date").toLocalDate(),
                                new ReservationTime(rs.getLong("time_id"), rs.getTime("time_start_at").toLocalTime()),
                                theme
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT r.id as reservation_id,
                       r.member_id as member_id,
                       r.date,                                       
                       rt.start_at,
                       rt.id as time_id,
                       t.id as theme_id,
                       t.name as theme_name,
                       t.description as theme_des,
                       t.thumbnail as theme_thumb
                    FROM reservation as r 
                    INNER JOIN reservation_time as rt ON rt.id = r.time_id
                    INNER JOIN theme as t ON t.id = r.theme_id
                    WHERE r.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = Theme.of(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        Long memberId = rs.getLong("member_id");
                        Member member = memberDao.findById(memberId)
                                .orElseThrow(() -> new NoSuchElementException("회원 정보 없음"));

                        return Reservation.of(
                                rs.getLong("reservation_id"),
                                member,
                                rs.getDate("date").toLocalDate(),
                                new ReservationTime(
                                        rs.getLong("time_id"),
                                        rs.getTime("start_at").toLocalTime()),
                                theme
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        String sql = """
                SELECT r.id as reservation_id, r.member_id, r.date,
                      rt.id as time_id, rt.start_at,
                      t.id as theme_id, t.name as theme_name, t.description as theme_des, t.thumbnail as theme_thumb
                FROM reservation as r 
                INNER JOIN reservation_time as rt ON rt.id = r.time_id 
                INNER JOIN theme as t ON r.theme_id = t.id
                WHERE r.date = ? and rt.start_at = ?""";
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        Theme theme = Theme.of(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("theme_des"),
                                rs.getString("theme_thumb")
                        );

                        Long memberId = rs.getLong("member_id");
                        Member member = memberDao.findById(memberId)
                                .orElseThrow(() -> new NoSuchElementException("회원 정보 없음"));

                        return Reservation.of(
                                rs.getLong("reservation_id"),
                                member,
                                rs.getDate("date").toLocalDate(),
                                new ReservationTime(rs.getLong("time_id"), rs.getTime("start_at").toLocalTime()),
                                theme
                        );
                    },
                    date,
                    time
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT rt.id as time_id, rt.start_at as time_start_at
                FROM reservation_time as rt
                WHERE rt.id NOT IN (
                    SELECT r.time_id
                    FROM reservation as r
                    WHERE r.date = ? AND r.theme_id = ?
                )
                ORDER BY rt.start_at
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) ->
                        new ReservationTime(
                                rs.getLong("time_id"),
                                rs.getTime("time_start_at").toLocalTime()
                        ),
                date,
                themeId
        );
    }

    @Override
    public List<Theme> findTop10Themes(LocalDate currentDate) {
        String sql = """
                SELECT t.*, COUNT(r.theme_id) as reservation_count
                FROM theme AS t
                JOIN reservation AS r ON r.theme_id = t.id
                WHERE r.date > ? AND r.date < ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY COUNT(r.theme_id) DESC
                LIMIT 10;
                """;

        LocalDate startDate = currentDate.minusDays(8);

        return jdbcTemplate.query(sql,
                new Object[]{startDate, currentDate},
                (rs, rowNum) ->
                        Theme.of(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("thumbnail")
                        )
        );
    }
}

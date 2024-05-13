package roomescape.reservation.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.ReservationMember;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ReservationDao {

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    new ReservationMember(resultSet.getLong("member_id"), resultSet.getString("member_name")),
                    resultSet.getObject("date", LocalDate.class),
                    new ReservationTime(resultSet.getLong("time_id"), resultSet.getObject("start_at", LocalTime.class)),
                    new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"), resultSet.getString("description"), resultSet.getString("thumbnail"))
            );

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setLong(1, reservation.getMemberId());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getReservationTimeId());
            ps.setLong(4, reservation.getThemeId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id,
                m.id as member_id,
                m.name as member_name,
                r.date,
                t.id as time_id,
                t.start_at,
                th.id as theme_id,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                inner join member as m on r.member_id = m.id
                """, reservationRowMapper);
        return Collections.unmodifiableList(reservations);
    }

    public List<Reservation> findByMemberIdAndThemeIdAndDateBetween(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id,
                m.id as member_id,
                m.name as member_name,
                r.date,
                t.id as time_id,
                t.start_at,
                th.id as theme_id,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                inner join member as m on r.member_id = m.id
                WHERE r.member_id = ? AND r.theme_id = ? AND r.date >= ? AND r.date <= ?
                """, reservationRowMapper, memberId, themeId, from, to);
        return Collections.unmodifiableList(reservations);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean existByDateTimeTheme(LocalDate date, LocalTime time, Long themeId) {
        int count = jdbcTemplate.queryForObject("""
                SELECT count(1) 
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id
                WHERE r.date = ? AND t.start_at = ? AND r.theme_id = ?
                """, Integer.class, date, time, themeId);
        return count > 0;
    }

    public List<Long> findTimeIdByDateThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.queryForList("SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?", Long.class, date, themeId);
    }

    public List<Long> find10ThemeIdsOrderByReservationThemeCountDescBetween(LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.queryForList("""
                SELECT r.theme_id
                FROM reservation AS r
                WHERE r.date >= ? AND r.date <= ?
                GROUP BY r.theme_id
                ORDER BY COUNT(r.theme_id) DESC
                LIMIT 10
                """, Long.class, startDate.toString(), endDate.toString());
    }
}

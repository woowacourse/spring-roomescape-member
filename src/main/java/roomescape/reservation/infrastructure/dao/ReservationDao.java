package roomescape.reservation.infrastructure.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.DeleteReservationException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation insert(final CreateReservationRequest request) {
        String sql = "insert into reservation (member_id, theme_id, date, time_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setLong(1, request.getMember().getId());
            ps.setLong(2, request.getTheme().getId());
            ps.setString(3, request.getDate().getReservationDate().toString());
            ps.setLong(4, request.getTime().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();

        return new Reservation(id, request.getMember(), request.getTheme(), request.getDate(), request.getTime());
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """
                    SELECT
                    r, rt, m
                FROM reservation as r
                inner join reservation_time as rt
                on r.time_id = rt.id
                inner join theme as t
                on r.theme_id = t.id
                inner join member as m 
                on m.id = r.member_id
                """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new Reservation(
                            resultSet.getLong("id"),
                            new Member(
                                    resultSet.getLong("m.id"),
                                    resultSet.getString("m.name"),
                                    resultSet.getString("m.email"),
                                    resultSet.getString("m.password"),
                                    Role.valueOf(resultSet.getString("m.role"))
                            ),
                            new Theme(
                                    resultSet.getLong("t.id"),
                                    resultSet.getString("t.name"),
                                    resultSet.getString("t.description"),
                                    resultSet.getString("t.thumbnail")
                            ),
                            new ReservationDate(
                                    resultSet.getDate("r.date").toLocalDate()
                            ),
                            new ReservationTime(
                                    resultSet.getLong("rt.id"),
                                    resultSet.getTime("rt.start_at").toLocalTime()
                            ));
                });
    }

    @Override
    public void delete(final Long id) {
        String sql = "delete from reservation where id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows != 1) {
            throw new DeleteReservationException("[ERROR] 삭제하지 못했습니다.");
        }
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return result == 1;
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return result == 1;
    }

    @Override
    public boolean existsByDateTime(LocalDateTime reservationDateTime) {
        LocalDate date = reservationDateTime.toLocalDate();
        LocalTime time = reservationDateTime.toLocalTime();
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    INNER JOIN reservation_time rt
                    ON r.time_id = rt.id
                    WHERE r.date = ?
                    AND rt.start_at = ?
                )
                """;

        int result = jdbcTemplate.queryForObject(sql, Integer.class, date.toString(), time.toString());
        return result == 1;
    }

    @Override
    public boolean existsByDateAndThemeIdAndTimeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.date = ?
                    AND r.time_id = ?
                    AND r.theme_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return result == 1;
    }
}

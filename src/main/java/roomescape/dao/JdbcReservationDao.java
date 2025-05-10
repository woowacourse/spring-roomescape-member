package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.mapper.ReservationMapper;

@Component
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id, r.date, 
                    member_id, m.name as member_name, m.email, m.password, m.role,
                    time_id, rt.start_at, 
                    theme_id, t.name as theme_name, t.description, t.thumbnail
                from reservation as r 
                inner join reservation_time as rt on r.time_id = rt.id
                inner join theme as t on r.theme_id = t.id
                inner join member as m on r.member_id = m.id
                """;
        return jdbcTemplate.query(
                sql,
                new ReservationMapper()
        );
    }

    @Override
    public Reservation create(Reservation newReservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (date, member_id, time_id, theme_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setObject(1, newReservation.getDate());
                    ps.setLong(2, newReservation.getMember().getId());
                    ps.setLong(3, newReservation.getTime().getId());
                    ps.setLong(4, newReservation.getTheme().getId());
                    return ps;
                },
                keyHolder
        );
        long reservationId = keyHolder.getKey().longValue();
        return newReservation.copyWithId(reservationId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        int deletedCount = jdbcTemplate.update(
                sql,
                id
        );
        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 예약 id입니다.");
        }
    }

    @Override
    public List<Reservation> findByThemeAndMemberAndDate(Long themeId, Long memberId, LocalDate dateFrom,
                                                         LocalDate dateTo) {
        String sql = """
                select r.id, r.date, 
                    member_id, m.name as member_name, m.email, m.password, m.role,
                    time_id, rt.start_at, 
                    theme_id, t.name as theme_name, t.description, t.thumbnail
                from reservation as r 
                inner join reservation_time as rt on r.time_id = rt.id
                inner join theme as t on r.theme_id = t.id
                inner join member as m on r.member_id = m.id
                where theme_id = ? and member_id = ? and date between ? and ?
                """;
        return jdbcTemplate.query(
                sql,
                new ReservationMapper(),
                themeId,
                memberId,
                dateFrom,
                dateTo
        );
    }

    @Override
    public List<Long> findMostReservedThemeIdsBetween(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    t.id AS theme_id
                FROM
                    theme t
                LEFT JOIN reservation r
                    ON t.id = r.theme_id
                    AND r.date BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY COUNT(r.id) DESC
                LIMIT 10;
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("theme_id"),
                startDate,
                endDate
        );
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = "select exists(select 1 from reservation where time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                timeId
        ));
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        String sql = "select exists(select 1 from reservation where theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                themeId
        ));
    }

    @Override
    public boolean existBySameDateTime(Reservation reservation) {
        String sql = "select exists(select 1 from reservation where date = ? AND time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                reservation.getDate(),
                reservation.getTime().getId()
        ));
    }

    @Override
    public boolean existByDateTimeAndTheme(LocalDate date, ReservationTime time, Long themeId) {
        String sql = "select exists(select 1 from reservation where date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                date,
                time.getId(),
                themeId
        ));
    }
}

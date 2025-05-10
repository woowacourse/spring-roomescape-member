package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.mapper.ReservationMapper;

@Component
public class JdbcReservationDao implements ReservationDao {

    private static final String RESERVATION_SELECT_SQL = """
            select r.id, r.date, time_id, rt.start_at, theme_id, t.name as theme_name, t.description, t.thumbnail, member_id, m.name as member_name, m.email, m.password, m.role 
            from reservation as r 
            inner join reservation_time as rt on r.time_id = rt.id
            inner join theme as t on r.theme_id = t.id
            inner join member as m on r.member_id = m.id
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                RESERVATION_SELECT_SQL,
                new ReservationMapper()
        );
    }

    @Override
    public List<Long> findTop10ByBetweenDates(LocalDate start, LocalDate end) {
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
                start,
                end
        );
    }

    @Override
    public List<Reservation> findFilterByThemeIdOrMemberIdOrDate(Long themeId, Long memberId, LocalDate dateFrom,
                                                                 LocalDate dateTo) {
        StringBuilder sql = new StringBuilder(RESERVATION_SELECT_SQL);
        sql.append("WHERE 1=1 ");
        List<Object> params = appendFilterCondition(themeId, memberId, dateFrom, dateTo, sql);

        return jdbcTemplate.query(
                sql.toString(),
                new ReservationMapper(),
                params.toArray()
        );
    }

    @Override
    public Long save(Reservation newReservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (date, time_id, theme_id, member_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setObject(1, newReservation.getDate());
                    ps.setLong(2, newReservation.getReservationTime().getId());
                    ps.setLong(3, newReservation.getTheme().getId());
                    ps.setLong(4, newReservation.getMember().getId());
                    return ps;
                },
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(
                sql,
                id
        );
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String sql = "select exists(select 1 from reservation where time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                timeId
        ));
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        String sql = "select exists(select 1 from reservation where theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                themeId
        ));
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, long timeId) {
        String sql = "select exists(select 1 from reservation where date = ? AND time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                date,
                timeId
        ));
    }

    private List<Object> appendFilterCondition(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo,
                                               StringBuilder sql) {
        List<Object> params = new ArrayList<>();

        if (themeId != null) {
            sql.append("AND (theme_id = ?) ");
            params.add(themeId);
        }

        if (memberId != null) {
            sql.append("AND (member_id = ?) ");
            params.add(memberId);
        }

        if (dateFrom != null) {
            sql.append("AND (date >= ?) ");
            params.add(dateFrom);
        }

        if (dateTo != null) {
            sql.append("AND (date <= ?) ");
            params.add(dateTo);
        }
        return params;
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "select exists(select 1 from reservation where date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                date,
                timeId,
                themeId
        ));
    }
}

package roomescape.reservation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation add(Reservation reservation) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", reservation.getMember().getId());
        params.put("date", reservation.getDate().getDate());
        params.put("time_id", reservation.getTime().getId());
        params.put("theme_id", reservation.getTheme().getId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Reservation.createWithId(id, reservation.getMember(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findFiltered(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder("""
                    SELECT
                        r.id AS reservation_id,
                        r.date AS reservation_date,
                        m.id AS member_id,
                        m.name AS member_name,
                        m.email AS member_email,
                        m.password AS member_password,
                        rt.id AS time_id,
                        rt.start_at AS time_value,
                        t.id AS theme_id,
                        t.name AS theme_name,
                        t.description AS theme_description,
                        t.thumbnail AS theme_thumbnail
                    FROM
                        reservation r
                    INNER JOIN member m ON r.member_id = m.id
                    INNER JOIN reservation_time rt ON r.time_id = rt.id
                    INNER JOIN theme t ON r.theme_id = t.id
                    WHERE 1=1
                """);

        List<Object> args = new ArrayList<>();

        if (memberId != null) {
            sql.append(" AND m.id = ?");
            args.add(memberId);
        }

        if (themeId != null) {
            sql.append(" AND t.id = ?");
            args.add(themeId);
        }

        if (from != null) {
            sql.append(" AND r.date >= ?");
            args.add(Date.valueOf(from));
        }

        if (to != null) {
            sql.append(" AND r.date <= ?");
            args.add(Date.valueOf(to));
        }

        return jdbcTemplate.query(
                sql.toString(),
                args.toArray(),
                (resultSet, rowNum) -> {
                    Member member = Member.createWithId(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("member_email"),
                            resultSet.getString("member_password"),
                            Role.findBy(resultSet.getString("role"))
                    );

                    ReservationDate date = new ReservationDate(resultSet.getDate("reservation_date").toLocalDate());

                    ReservationTime time = ReservationTime.createWithId(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    );

                    Theme theme = Theme.createWithId(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    );

                    return Reservation.createWithId(
                            resultSet.getLong("reservation_id"),
                            member,
                            date,
                            time,
                            theme
                    );
                }
        );
    }


    @Override
    public boolean existsByDateAndTime(LocalDate date, Long id) {
        String sql = "select exists(select 1 from reservation where date = ? and time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date, id));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "select exists(select 1 from reservation where time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    @Override
    public List<Long> findTimeIdsByDateAndTheme(LocalDate date, Long themeId) {
        String sql = "SELECT time_id from reservation where date=? and theme_id=?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date,
                themeId);
    }

    @Override
    public List<Long> findThemeIdsOrderByReservationCountBetween(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                SELECT theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(?) DESC
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("theme_id"),
                startDate,
                endDate,
                limit
        );
    }
}

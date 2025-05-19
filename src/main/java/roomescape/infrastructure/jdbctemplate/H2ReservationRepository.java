package roomescape.infrastructure.jdbctemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.persistence.ReservationRepository;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public H2ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> (
            new Reservation(
                    rs.getLong("id"),
                    new Member(
                            rs.getLong("member_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            MemberRole.valueOf(rs.getString("role"))
                    ),
                    rs.getObject("date", LocalDate.class),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getObject("start_at", LocalTime.class)
                    ),
                    new ReservationTheme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    )
            )
    );

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT
                    r.id AS reservation_id, r.date,
                    m.id AS member_id, m.name, m.email, m.password, m.role,
                    t.id AS time_id, t.start_at,
                    th.id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                JOIN theme AS th
                JOIN member as m
                ON r.time_id = t.id AND r.theme_id = th.id AND m.id = r.member_id
                """;
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String query = """
                SELECT
                    r.id AS reservation_id, r.date,
                    m.id AS member_id, m.name, m.email, m.password, m.role,
                    t.id AS time_id, t.start_at,
                    th.id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                JOIN theme AS th
                JOIN member as m
                ON r.time_id = t.id AND r.theme_id = th.id AND m.id = r.member_id
                WHERE r.id = ?
                """;
        return jdbcTemplate.query(query, reservationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAllByThemeAndMemberAndDate(Long themeId,
                                                            Long memberId,
                                                            LocalDate dateFrom,
                                                            LocalDate dateTo) {
        String query = """
                SELECT
                    r.id AS reservation_id, r.date,
                    m.id AS member_id, m.name, m.email, m.password, m.role,
                    t.id AS time_id, t.start_at,
                    th.id AS theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                JOIN theme AS th
                JOIN member as m
                ON r.time_id = t.id AND r.theme_id = th.id AND m.id = r.member_id
                WHERE th.id = ? AND m.id = ? AND r.date >= ? AND r.date <= ?
                """;
        return jdbcTemplate.query(query, reservationRowMapper,
                themeId,
                memberId,
                dateFrom,
                dateTo
        );
    }

    @Override
    public Long add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", reservation.getMember().getId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        return (Long) jdbcInsert.executeAndReturnKey(parameters);
    }

    @Override
    public void deleteById(Long id) {
        String query = """
                DELETE FROM reservation 
                WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    @Override
    public boolean existsByReservation(Reservation reservation) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    JOIN reservation_time t
                    JOIN theme th
                        ON r.time_id = t.id AND r.theme_id = th.id
                    WHERE r.date = ? AND t.start_at = ? AND r.theme_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                        query,
                        Boolean.class,
                        reservation.getDate(),
                        reservation.getTime().getStartAt(),
                        reservation.getTheme().getId()
                )
        );
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }
}

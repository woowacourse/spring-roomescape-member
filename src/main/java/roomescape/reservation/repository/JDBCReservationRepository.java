package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

@Repository
public class JDBCReservationRepository implements ReservationRepository {
    private static final String BASE_SELECT =
            "SELECT r.id AS reservation_id, r.date, " +
                    "t.id AS time_id, t.start_at AS time_value, " +
                    "m.id AS member_id, m.name AS member_name, m.email AS member_email, " +
                    "m.password AS member_password, m.role AS member_role " +
                    "FROM reservation AS r " +
                    "INNER JOIN reservation_time AS t ON r.time_id = t.id " +
                    "INNER JOIN theme AS th ON r.theme_id = th.id " +
                    "INNER JOIN member AS m ON r.member_id = m.id ";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final ReservationRowMapper rowMapper = new ReservationRowMapper();

    public JDBCReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(BASE_SELECT, rowMapper);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("date", reservation.getDate(),
                        "member_id", reservation.getMember().getId(),
                        "time_id", reservation.getTime().getId(),
                        "theme_id", reservation.getTheme().getId())
        ).longValue();

        return Reservation.of(generatedId, reservation.getDate(), reservation.getMember(),
                reservation.getTime(), reservation.getTheme());
    }

    @Override
    public boolean deleteById(final Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) > 0;
    }

    @Override
    public boolean existsByTimeId(final Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)",
                Boolean.class,
                id
        ));
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)",
                Boolean.class,
                id
        ));
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)",
                Boolean.class,
                date,
                timeId
        ));
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        String query = BASE_SELECT + "WHERE r.date = ? AND r.theme_id = ?";
        return jdbcTemplate.query(query, rowMapper, date, themeId);
    }

    @Override
    public List<Reservation> searchByFilters(final Long themeId, final Long memberId,
                                             final LocalDate dateFrom, final LocalDate dateTo) {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (themeId != null) {
            conditions.add("r.theme_id = ?");
            params.add(themeId);
        }
        if (memberId != null) {
            conditions.add("r.member_id = ?");
            params.add(memberId);
        }
        if (dateFrom != null) {
            conditions.add("r.date >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null) {
            conditions.add("r.date <= ?");
            params.add(dateTo);
        }

        String whereClause = conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions);
        String query = BASE_SELECT + whereClause;

        return jdbcTemplate.query(query, rowMapper, params.toArray());
    }
}

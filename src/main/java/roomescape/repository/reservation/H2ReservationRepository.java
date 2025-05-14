package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.other.ReservationSearchCondition;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> mapper;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insertActor;

    static {
        mapper = (resultSet, resultNumber) -> new Reservation(
                resultSet.getLong("reservation_id"),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        resultSet.getString("member_password"),
                        MemberRole.valueOf(resultSet.getString("member_role"))
                ),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }

    public H2ReservationRepository(DataSource dataSource, JdbcTemplate template) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.template = template;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role As member_role,
                       rt.id AS time_id, rt.start_at,
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON r.theme_id = t.id
                ORDER BY r.id DESC
                """;
        return template.query(sql, mapper);
    }

    @Override
    public List<Reservation> findAllByFilter(ReservationSearchCondition condition) {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role As member_role,
                       rt.id AS time_id, rt.start_at,
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON r.theme_id = t.id
                WHERE r.theme_id = ?
                AND r.member_id = ?
                AND r.date >= ?
                AND r.date <= ?
                ORDER BY r.id DESC
                """;
        return template.query(sql, mapper,
                condition.themeId(), condition.memberId(), condition.dateFrom(), condition.dateTo());
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.name AS member_name, m.email AS member_email, m.password AS member_password, m.role As member_role,
                       rt.id AS time_id, rt.start_at,
                       t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN theme AS t ON r.theme_id = t.id
                WHERE r.id = ?
                """;
        try {
            Reservation reservation = template.queryForObject(sql, mapper, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT * 
                    FROM reservation AS r
                    INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                    WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public boolean checkExistenceInTime(long reservationTimeId) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT * 
                    FROM reservation AS r 
                    INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                    WHERE r.time_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    @Override
    public boolean checkExistenceInTheme(long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT * 
                    FROM reservation AS r 
                    INNER JOIN theme AS t ON r.theme_id = t.id 
                    WHERE r.theme_id = ?)
                """;
        return template.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public long add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", reservation.getMember().getId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE reservation.id = ?";
        template.update(sql, id);
    }
}

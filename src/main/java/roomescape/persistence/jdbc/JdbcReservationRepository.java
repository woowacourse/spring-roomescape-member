package roomescape.persistence.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.Reservation;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.entity.MemberEntity;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.persistence.entity.ReservationThemeEntity;
import roomescape.persistence.entity.ReservationTimeEntity;
import roomescape.presentation.admin.dto.ReservationQueryCondition;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT
                    r.id AS reservation_id, 
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS member_email,
                    m.role AS member_role,
                    r.date,
                    t.id AS time_id, 
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name, 
                    th.description, 
                    th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                JOIN theme AS th
                JOIN member AS m
                ON r.time_id = t.id AND r.theme_id = th.id AND r.member_id = m.id
                """;
        List<ReservationEntity> reservationEntities = jdbcTemplate.query(
                query,
                (rs, rowNum) -> new ReservationEntity(
                        rs.getLong("reservation_id"),
                        new MemberEntity(
                                rs.getLong("member_id"),
                                rs.getString("member_name"),
                                rs.getString("member_email"),
                                rs.getString("member_role")
                        ),
                        rs.getString("date"),
                        new ReservationTimeEntity(
                                rs.getLong("time_id"),
                                rs.getString("start_at")
                        ),
                        new ReservationThemeEntity(
                                rs.getLong("theme_id"),
                                rs.getString("theme_name"),
                                rs.getString("description"),
                                rs.getString("thumbnail")
                        )
                )
        );
        return reservationEntities.stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String query = """
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS member_email,
                    m.role AS member_role,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t
                JOIN theme AS th
                JOIN member AS m
                ON r.time_id = t.id AND r.theme_id = th.id AND r.member_id = m.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new ReservationEntity(
                                rs.getLong("reservation_id"),
                                new MemberEntity(
                                        rs.getLong("member_id"),
                                        rs.getString("member_name"),
                                        rs.getString("member_email"),
                                        rs.getString("member_role")
                                ),
                                rs.getString("date"),
                                new ReservationTimeEntity(
                                        rs.getLong("time_id"),
                                        rs.getString("start_at")
                                ),
                                new ReservationThemeEntity(
                                        rs.getLong("theme_id"),
                                        rs.getString("theme_name"),
                                        rs.getString("description"),
                                        rs.getString("thumbnail")
                                )
                        ),
                        id
                )
                .stream()
                .findFirst()
                .map(ReservationEntity::toDomain);
    }

    @Override
    public Reservation add(Reservation reservation) {
        ReservationEntity reservationEntity = ReservationEntity.fromDomain(reservation);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", reservationEntity.getMemberEntity().getId());
        parameters.put("date", reservationEntity.getDate());
        parameters.put("time_id", reservationEntity.getTimeEntity().getId());
        parameters.put("theme_id", reservationEntity.getThemeEntity().getId());
        long id = jdbcInsert.executeAndReturnKey(parameters).longValue();
        return reservationEntity.copyWithId(id).toDomain();
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
    public boolean existsByThemeId(Long themeId) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    @Override
    public List<Reservation> findAllByCondition(ReservationQueryCondition condition) {
        StringBuilder query = new StringBuilder("""
                SELECT
                    r.id AS reservation_id,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS member_email,
                    m.role AS member_role,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation AS r
                JOIN reservation_time AS t ON r.time_id = t.id
                JOIN theme AS th ON r.theme_id = th.id
                JOIN member AS m ON r.member_id = m.id
                WHERE 1=1
                """);
        List<Object> params = getDynamicQueryCondition(condition, query);
        return jdbcTemplate.query(
                        query.toString(),
                        (rs, rowNum) -> new ReservationEntity(
                                rs.getLong("reservation_id"),
                                new MemberEntity(
                                        rs.getLong("member_id"),
                                        rs.getString("member_name"),
                                        rs.getString("member_email"),
                                        rs.getString("member_role")
                                ),
                                rs.getString("date"),
                                new ReservationTimeEntity(
                                        rs.getLong("time_id"),
                                        rs.getString("start_at")
                                ),
                                new ReservationThemeEntity(
                                        rs.getLong("theme_id"),
                                        rs.getString("theme_name"),
                                        rs.getString("description"),
                                        rs.getString("thumbnail")
                                )
                        ),
                        params.toArray()
                )
                .stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    private List<Object> getDynamicQueryCondition(ReservationQueryCondition condition, StringBuilder query) {
        List<Object> params = new ArrayList<>();
        if (condition.themeId() != null) {
            query.append(" AND th.id = ?");
            params.add(condition.themeId());
        }
        if (condition.memberId() != null) {
            query.append(" AND m.id = ?");
            params.add(condition.memberId());
        }
        if (condition.dateFrom() != null) {
            query.append(" AND r.date >= ?");
            params.add(condition.dateFrom());
        }
        if (condition.dateTo() != null) {
            query.append(" AND r.date <= ?");
            params.add(condition.dateTo());
        }
        return params;
    }
}

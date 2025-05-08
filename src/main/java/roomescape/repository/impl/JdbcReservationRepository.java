package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.NotAbleDeleteException;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> readAll() {
        final String query = """
                SELECT
                    r.id,
                    m.id AS member_id, m.name AS member_name, m.email AS member_email,
                    r.date,
                    t.id AS time_id, t.start_at,
                    th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail
                FROM reservation r
                JOIN member m ON r.member_id = m.id
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("id"),
                        new Member(
                                resultSet.getLong("member_id"),
                                resultSet.getString("member_name"),
                                resultSet.getString("member_email")
                        ),
                        resultSet.getDate("date").toLocalDate(),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                resultSet.getTime("start_at").toLocalTime()
                        ),
                        new Theme(
                                resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("theme_description"),
                                resultSet.getString("theme_thumbnail")
                        )
                )
        );
    }

    public boolean existsByTimeId(Long timeId) {
        final String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;

        return jdbcTemplate.queryForObject(query, Boolean.class, timeId);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("member_id", reservation.getMember().getId()),
                Map.entry("date", reservation.getDate()),
                Map.entry("time_id", reservation.getTime().getId()),
                Map.entry("theme_id", reservation.getTheme().getId())
        );

        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return Reservation.generateWithPrimaryKey(reservation, generatedKey);
    }

    public void delete(Long id) {
        final String query = "DELETE FROM reservation WHERE id = ?";
        int affectedRows = jdbcTemplate.update(query, id);
        if (affectedRows == 0) {
            throw new NotAbleDeleteException("예약을 삭제할 수 없습니다. 존재하지 않는 예약입니다.");
        }
    }
}

package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.NotAbleDeleteException;
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

    public List<Reservation> readReservations() {
        final String query = """
                SELECT
                    r.id as reservation_id,
                    r.name as reservation_name,
                    r.date as reservation_date,
                    t.id as time_id,
                    t.start_at as time_start_at,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail 
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                """;

        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getString("reservation_name"),
                        resultSet.getDate("reservation_date").toLocalDate(),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                resultSet.getTime("time_start_at").toLocalTime()
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

    public Reservation saveReservation(Reservation reservation) {
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("name", reservation.getName()),
                Map.entry("date", reservation.getDate()),
                Map.entry("time_id", reservation.getTime().getId()),
                Map.entry("theme_id", reservation.getTheme().getId())
        );

        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return Reservation.generateWithPrimaryKey(reservation, generatedKey);
    }

    public void deleteReservation(Long id) {
        final String query = "DELETE FROM reservation WHERE id = ?";
        int affectedRows = jdbcTemplate.update(query, id);
        if (affectedRows == 0) {
            throw new NotAbleDeleteException("예약을 삭제할 수 없습니다. 존재하지 않는 예약입니다.");
        }
    }
}

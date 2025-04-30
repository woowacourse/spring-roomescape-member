package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.entity.ReservationEntity;
import roomescape.entity.ReservationTimeEntity;
import roomescape.entity.ThemeEntity;

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
    public ReservationEntity add(Reservation reservation) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", reservation.getName());
        params.put("date", reservation.getDate());
        params.put("time_id", reservation.getTime().getId());
        params.put("theme_id", reservation.getTheme().getId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationEntity.of(id, reservation);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<ReservationEntity> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    rt.id AS time_id,
                    rt.start_at AS time_value,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM
                    reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id;
                
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    ReservationTimeEntity reservationTimeEntity = new ReservationTimeEntity(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    );

                    ThemeEntity themeEntity = new ThemeEntity(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail"));

                    ReservationEntity reservationEntity = new ReservationEntity(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("reservation_name"),
                            resultSet.getDate("reservation_date").toLocalDate(),
                            reservationTimeEntity,
                            themeEntity
                    );
                    return reservationEntity;
                }
        );
    }

    @Override
    public boolean existsByDateAndTime(LocalDate date, Long id) {
        String sql = "select count(*) from reservation where date = ? and time_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, date, id);
        return count != 0;
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != 0;
    }

    @Override
    public List<Long> findTimeIdsByDateAndTheme(LocalDate date, Long themeId) {
        String sql = "SELECT time_id from reservation where date=? and theme_id=?";
        List<Long> timeIds = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date,
                themeId);
        return timeIds;
    }

    @Override
    public List<Long> findTopThemesByReservationCountBetween(LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT theme_id
                FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY COUNT(*) DESC
                LIMIT 10
                """;
        List<Long> themeIds = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long themeId = resultSet.getLong("theme_id");
            return themeId;
        }, startDate, endDate);
        return themeIds;
    }
}

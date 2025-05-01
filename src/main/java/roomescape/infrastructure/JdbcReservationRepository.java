package roomescape.infrastructure;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final static RowMapper<Reservation> RESERVATION_ROW_MAPPER =
            (rs, rowNum) -> {
                Long id = rs.getLong("reservation_id");
                String name = rs.getString("reservation_name");
                LocalDate date = rs.getObject("reservation_date", LocalDate.class);
                Long time_id = rs.getLong("time_id");
                LocalTime time = rs.getObject("time_value", LocalTime.class);
                long theme_id = rs.getLong("theme_id");
                String theme_name = rs.getString("theme_name");
                String theme_description = rs.getString("theme_description");
                String theme_thumbnail = rs.getString("theme_thumbnail");
                return Reservation.of(
                        id,
                        name,
                        Theme.of(theme_id, theme_name, theme_description, theme_thumbnail),
                        date,
                        ReservationTime.of(time_id, time)
                );
            };
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String findAllSql = """
                SELECT
                    r.id as reservation_id,
                    r.name as reservation_name,
                    r.date as reservation_date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation as r
                join reservation_time as t
                    on r.time_id = t.id
                join theme as th 
                    on r.theme_id = th.id
                """;
        return jdbcTemplate.query(findAllSql, RESERVATION_ROW_MAPPER);
    }

    @Override
    public Long save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = Map.of(
                "name", reservation.getName(),
                "date", Date.valueOf(reservation.getReservationDate()),
                "time_id", reservation.getReservationTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public boolean deleteById(Long id) {
        String deleteSql = "DELETE FROM reservation WHERE id=?";
        return jdbcTemplate.update(deleteSql, id) > 0;
    }
}

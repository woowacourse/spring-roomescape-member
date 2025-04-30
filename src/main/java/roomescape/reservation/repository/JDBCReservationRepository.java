package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

@Repository
public class JDBCReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public JDBCReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> getAll() {
        return jdbcTemplate.query(
                "SELECT "
                        + "r.id as reservation_id, "
                        + "r.name, "
                        + "r.date, "
                        + "t.id as time_id, "
                        + "t.start_at as time_value, "
                        + "th.id as theme_id, "
                        + "th.name as theme_name, "
                        + "th.description,"
                        + "th.thumbnail "
                        + "FROM reservation as r "
                        + "inner join reservation_time as t "
                        + "on r.time_id = t.id "
                        + "inner join theme as th "
                        + "on r.theme_id = th.id",
                (resultSet, rowNum) -> {
                    ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                            resultSet.getLong("time_id"),
                            resultSet.getString("time_value"));

                    ThemeEntity themeEntity = new ThemeEntity(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail"));

                    ReservationEntity entity = new ReservationEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("date"),
                            timeEntity,
                            themeEntity
                    );
                    return entity.toReservation();
                }
        );
    }

    @Override
    public Reservation put(final Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", reservation.getName(), "date", reservation.getDate(), "time_id",
                        reservation.getTime().getId(), "theme_id", reservation.getTheme().getId())
        ).longValue();

        return Reservation.of(generatedId, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public boolean deleteById(final Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) != 0;
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
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE (date, time_id) = (?,?))",
                Boolean.class,
                date,
                timeId
        ));
    }

}

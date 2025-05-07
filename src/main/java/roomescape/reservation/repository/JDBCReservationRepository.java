package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

@Repository
public class JDBCReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_WITH_JOIN = "SELECT "
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
            + "on r.theme_id = th.id";

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> {
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                resultSet.getLong("time_id"),
                resultSet.getString("time_value"));

        ThemeEntity themeEntity = new ThemeEntity(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));

        ReservationEntity entity = new ReservationEntity(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                timeEntity,
                themeEntity
        );
        return entity.toReservation();
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                SELECT_RESERVATION_WITH_JOIN,
                RESERVATION_ROW_MAPPER
        );
    }

    @Override
    public Reservation save(final Reservation reservation) {
        Long generatedId = simpleJdbcInsert.executeAndReturnKey(
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
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)",
                    Boolean.class,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)",
                    Boolean.class,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final Long timeId) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE (date, time_id) = (?,?))",
                    Boolean.class,
                    date,
                    timeId
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        return jdbcTemplate.query(
                SELECT_RESERVATION_WITH_JOIN + " where (date, theme_id) = (?, ?)",
                RESERVATION_ROW_MAPPER,
                date,
                themeId
        );
    }
}

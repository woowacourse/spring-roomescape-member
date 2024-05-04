package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@Repository
public class ReservationJdbcDao implements ReservationDao {

    public static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum)
            -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date")
                        .toLocalDate(),
                new Time(resultSet.getLong("time_id"),
                        resultSet.getTime("start_at")
                                .toLocalTime()),
                new Theme(resultSet.getLong("theme_id"),
                        resultSet.getString("themeName"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId());

        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        reservation.setIdOnSave(id);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        String findReservationSql = """
                SELECT r.id, r.name, r.date, 
                t.id AS time_id, t.start_at, 
                th.id AS theme_id, th.name AS themeName, th.description, th.thumbnail 
                FROM reservation r 
                INNER JOIN reservation_time t ON r.time_id = t.id 
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ? 
                """;

        try {
            Reservation reservation = jdbcTemplate.queryForObject(findReservationSql, RESERVATION_ROW_MAPPER,
                    reservationId);

            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAllReservationOrderByDateAndTimeStartAt() {
        String findAllReservationSql =
                """
                SELECT r.id, r.name, r.date, 
                t.id AS time_id, t.start_at, 
                th.id AS theme_id, th.name AS themeName, th.description, th.thumbnail 
                FROM reservation r 
                INNER JOIN reservation_time t ON r.time_id = t.id 
                INNER JOIN theme th ON r.theme_id = th.id 
                ORDER BY r.date ASC, t.start_at ASC
                """;

        return jdbcTemplate.query(findAllReservationSql, RESERVATION_ROW_MAPPER);
    }

    @Override
    public List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date) {
        String findAllByThemeIdAndDateSql =
                """
                SELECT r.id, r.name, r.date, 
                t.id AS timeId, t.start_at, 
                th.id AS themeId, th.name AS themeName, th.description, th.thumbnail 
                FROM reservation r 
                INNER JOIN reservation_time t ON r.time_id = t.id 
                INNER JOIN theme th ON r.theme_id = th.id 
                WHERE r.date = ? AND r.theme_id = ?
                ORDER BY r.date ASC, t.start_at ASC
                """;

        return jdbcTemplate.query(findAllByThemeIdAndDateSql, RESERVATION_ROW_MAPPER, date, themeId);
    }

    @Override
    public List<Theme> findThemeByDateOrderByThemeIdCount(LocalDate startDate, LocalDate endDate) {
        String findThemesInOrderSql =
                """
                SELECT th.id, th.name, th.thumbnail, th.description
                FROM theme th
                INNER JOIN (
                    SELECT theme_id
                    FROM reservation
                    WHERE date BETWEEN ? AND ?
                    GROUP BY theme_id
                    ORDER BY COUNT(theme_id) DESC
                ) r ON th.id = r.theme_id;
                """;

        return jdbcTemplate.query(findThemesInOrderSql, (resultSet, rowNum)
                -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                        )
                    ,startDate.toString(), endDate.toString());
    }

    @Override
    public void deleteById(long reservationId) {
        String deleteReservationSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteReservationSql, reservationId);
    }

    @Override
    public int countByTimeId(long timeId) {
        String findByTimeIdSql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(findByTimeIdSql, Integer.class, timeId);
    }
}

package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@Component
public class ReservationJdbcDao implements ReservationDao {
    public static final String TABLE_NAME = "reservation";
    public static final String TABLE_KEY = "id";
    public static final String RESERVATION_NAME_ATTRIBUTE = "name";
    public static final String RESERVATION_DATE_ATTRIBUTE = "date";
    public static final String TIME_TABLE_KEY = "timeId";
    public static final String TIME_TABLE_START_TIME_ATTRIBUTE = "start_at";
    public static final String THEME_TABLE_NAME_ATTRIBUTE = "name";
    public static final String THEME_TABLE_THUMBNAIL_ATTRIBUTE = "thumbnail";
    public static final String THEME_TABLE_DESCRIPTION_ATTRIBUTE = "description";

    public static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum)
            -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date")
                        .toLocalDate(),
                new Time(resultSet.getLong("time_id"),
                        resultSet.getTime(TIME_TABLE_START_TIME_ATTRIBUTE)
                                .toLocalTime()),
                new Theme(resultSet.getLong("theme_id"),
                        resultSet.getString("themeName"),
                        resultSet.getString(THEME_TABLE_DESCRIPTION_ATTRIBUTE),
                        resultSet.getString(THEME_TABLE_THUMBNAIL_ATTRIBUTE)
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(TABLE_KEY);
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId());

        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        reservation.setId(id);
        return reservation;
    }

    @Override
    public List<Reservation> findAllReservationOrderByDateAndTimeStartAt() {
        String findAllReservationSql =
                """
                SELECT r.id, r.name, r.date, 
                t.id AS timeId, t.start_at, 
                th.id AS themeId, th.name AS themeName, th.description, th.thumbnail 
                FROM reservation r 
                INNER JOIN reservation_time t ON r.time_id = t.id 
                INNER JOIN theme th ON r.theme_id = th.id 
                ORDER BY r.date ASC, t.start_at ASC
                """;

        return jdbcTemplate.query(findAllReservationSql, RESERVATION_ROW_MAPPER);
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

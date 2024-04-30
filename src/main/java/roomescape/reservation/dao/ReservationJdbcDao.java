package roomescape.reservation.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.Time;

@Component
public class ReservationJdbcDao implements ReservationDao {
    public static final String TABLE_NAME = "reservation";
    public static final String TABLE_KEY = "id";
    public static final String RESERVATION_NAME_ATTRIBUTE = "name";
    public static final String RESERVATION_DATE_ATTRIBUTE = "date";
    public static final String TIME_TABLE_KEY = "timeId";
    public static final String TIME_TABLE_START_TIME_ATTRIBUTE = "start_at";

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
                .addValue(RESERVATION_NAME_ATTRIBUTE, reservation.getName())
                .addValue(RESERVATION_DATE_ATTRIBUTE, reservation.getDate())
                .addValue(TIME_TABLE_KEY, reservation.getReservationTime().getId());

        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        reservation.setId(id);
        return reservation;
    }

    @Override
    public List<Reservation> findAllOrderByDateAndReservationTime() {
        String findAllReservationSql = "SELECT r.id, r.name, r.`date`, t.id AS timeId, t.start_at "
                + "FROM reservation r "
                + "INNER JOIN reservation_time t "
                + "ON r.time_id = t.id "
                + "ORDER BY r.date ASC , t.`start_at` ASC";
        return jdbcTemplate.query(findAllReservationSql, (resultSet, rowNum) -> new Reservation(
                resultSet.getLong(TABLE_KEY),
                resultSet.getString(RESERVATION_NAME_ATTRIBUTE),
                resultSet.getDate(RESERVATION_DATE_ATTRIBUTE).toLocalDate(),
                new Time(resultSet.getLong(TIME_TABLE_KEY),
                        resultSet.getTime(TIME_TABLE_START_TIME_ATTRIBUTE).toLocalTime())));
    }

    @Override
    public void deleteById(long reservationId) {
        String deleteReservationSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteReservationSql, reservationId);
    }

    @Override
    public int findByTimeId(long timeId) {
        String findByTimeIdSql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(findByTimeIdSql, Integer.class, timeId);
    }
}

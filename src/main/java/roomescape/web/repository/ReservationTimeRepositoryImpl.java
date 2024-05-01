package roomescape.web.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.ReservationTime;
import roomescape.core.dto.BookingTimeResponseDto;
import roomescape.core.repository.ReservationTimeRepository;

@Repository
public class ReservationTimeRepositoryImpl implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final ReservationTime reservationTime) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAtString());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", getReservationTimeRowMapper());
    }

    @Override
    public List<BookingTimeResponseDto> findAllByDateAndThemeId(final String date, final long themeId) {
        final String query = """
            SELECT t.id, t.start_at, r.id IS NOT NULL AS already_booked
            FROM reservation_time AS t 
            LEFT JOIN (
                SELECT * FROM reservation WHERE date LIKE ? AND theme_id = ?
            ) AS r
            ON t.id = r.time_id;
            """;
        return jdbcTemplate.query(query, getBookingTimeRowMapper(), date, themeId);
    }

    private RowMapper<BookingTimeResponseDto> getBookingTimeRowMapper() {
        return (resultSet, rowNum) -> {
            final Long timeId = resultSet.getLong("id");
            final String timeValue = resultSet.getString("start_at");
            final boolean alreadyBooked = resultSet.getBoolean("already_booked");

            return new BookingTimeResponseDto(timeId, timeValue, alreadyBooked);
        };
    }

    @Override
    public ReservationTime findById(final long id) {
        try {
            final String query = "SELECT id, start_at FROM reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(query, getReservationTimeRowMapper(), id);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Reservation time not found");
        }
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            final Long timeId = resultSet.getLong("id");
            final String timeValue = resultSet.getString("start_at");

            return new ReservationTime(timeId, timeValue);
        };
    }

    @Override
    public Integer countByStartAt(final String startAt) {
        final String query = """
                SELECT count(*)
                FROM reservation_time as t
                WHERE t.start_at = ?
                """;

        return jdbcTemplate.queryForObject(query, Integer.class, startAt);
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}

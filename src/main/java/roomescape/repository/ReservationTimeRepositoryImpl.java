package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithStateDto;
import roomescape.exception.NotFoundException;

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
    public ReservationTime save(final ReservationTime reservationTime) {
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAtString());
        final long id = jdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(
                id,
                reservationTime.getStartAt()
        );
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", getReservationTimeRowMapper());
    }

    @Override
    public List<ReservationTimeWithStateDto> findAllWithReservationState(final String date, final long themeId) {
        final String query = """
                SELECT t.id, t.start_at, r.id IS NOT NULL AS already_booked
                FROM reservation_time AS t 
                LEFT JOIN (
                    SELECT * FROM reservation WHERE date LIKE ? AND theme_id = ?
                ) AS r
                ON t.id = r.time_id;
                """;
        return jdbcTemplate.query(query, getReservationTimeWithStateRowMapper(), date, themeId);
    }

    @Override
    public ReservationTime findById(final long id) {
        try {
            final String query = "SELECT id, start_at FROM reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(query, getReservationTimeRowMapper(), id);
        } catch (final DataAccessException e) {
            throw new NotFoundException("예약을 찾을 수 없습니다.");
        }
    }

    @Override
    public boolean hasDuplicateReservationTime(final String startAt) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation_time WHERE start_at = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, startAt));
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getString("start_at")
        );
    }

    private RowMapper<ReservationTimeWithStateDto> getReservationTimeWithStateRowMapper() {
        return (resultSet, rowNum) -> new ReservationTimeWithStateDto(
                resultSet.getLong("id"),
                resultSet.getString("start_at"),
                resultSet.getBoolean("already_booked")
        );
    }
}

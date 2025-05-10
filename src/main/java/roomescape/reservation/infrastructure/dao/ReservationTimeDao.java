package roomescape.reservation.infrastructure.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.application.exception.DeleteTimeException;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class ReservationTimeDao implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime insert(final LocalTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setString(1, reservationTime.toString());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();

        return new ReservationTime(id, reservationTime);
    }

    @Override
    public List<ReservationTime> findAllTimes() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new ReservationTime(
                            resultSet.getLong("id"),
                            resultSet.getTime("start_at").toLocalTime()
                    );
                });
    }

    @Override
    public Optional<ReservationTime> findById(final Long timeId) {
        String sql = "select id, start_at from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) -> {
                        return new ReservationTime(
                                resultSet.getLong("id"),
                                resultSet.getTime("start_at").toLocalTime()
                        );
                    }, timeId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(final Long id) {
        String sql = "delete from reservation_time where id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows != 1) {
            throw new DeleteTimeException("[ERROR] 삭제하지 못했습니다.");
        }
    }

    @Override
    public boolean isExists(LocalTime startAt) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation_time
                    WHERE start_at = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, startAt.toString());
        return result == 1;
    }

}

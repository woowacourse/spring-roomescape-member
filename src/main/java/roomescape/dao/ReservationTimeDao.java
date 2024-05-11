package roomescape.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(String startAt) {
        String insertSql = "INSERT INTO reservation_time(start_at) VALUES ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, startAt);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<ReservationTime> findById(Long id) {
        String findByIdSql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        List<ReservationTime> reservationTimes = jdbcTemplate.query(findByIdSql, getReservationTimeRowMapper(), id);

        return Optional.ofNullable(DataAccessUtils.singleResult(reservationTimes));
    }

    public List<ReservationTime> findAll() {
        String findAllSql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(findAllSql,
                getReservationTimeRowMapper());
    }

    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, numRow) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getString("start_at")
        );
    }
}

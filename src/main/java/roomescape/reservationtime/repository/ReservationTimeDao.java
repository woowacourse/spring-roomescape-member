package roomescape.reservationtime.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ReservationTime save(ReservationTime time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO reservation_time(start_at) VALUES(?)",
                            new String[]{"id"});
                    ps.setTime(1, Time.valueOf(time.getStartAt()));
                    return ps;
                }
                , keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new ReservationTime(id, time.getStartAt());
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                (rs, rowNum) -> {
                    return new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                }
        );
    }

    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(
                "DELETE FROM reservation_time WHERE id = ?",
                id
        );
        return deletedRow == 1;
    }

    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    "SELECT id, start_at FROM reservation_time WHERE id = ?",
                    (rs, rowNum) -> {
                        return new ReservationTime(
                                rs.getLong("id"),
                                rs.getTime("start_at").toLocalTime()
                        );
                    },
                    id
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean isExistTime(LocalTime time) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM RESERVATION_TIME WHERE start_at = ?)
                """;
        return jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                time
        );
    }

    public int countTotalReservationTimes() {
        String sql = """
                SELECT COUNT(*) FROM RESERVATION_TIME
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<ReservationTime> findReservationTimesWithPage(int startRowNumber, int endRowNumber) {
        String sql = """
                SELECT t.id, t.start_at
                    FROM (
                        SELECT ROW_NUMBER() OVER() as row_num, * 
                        FROM RESERVATION_TIME
                    ) as t
                WHERE t.row_num BETWEEN ? AND ?
                ORDER BY t.row_num
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime()
                ),
                startRowNumber,
                endRowNumber
        );
    }
}

package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.AvailableTimeResponse;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ReservationTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, new String[]{"id"}
            );
            ps.setString(1, String.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, createReservationTimeRowMapper(), id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";

        return jdbcTemplate.query(sql, createReservationTimeRowMapper());
    }

    public Optional<ReservationTime> findReservationInSameId(Long id) {
        String sql = """
                select t.id, t.start_at
                from reservation_time t
                join reservation r
                on r.time_id = t.id
                where t.id = ?
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, createReservationTimeRowMapper(), id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<AvailableTimeResponse> findAvailableTime(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                t.id,
                t.start_at,
                  (CASE
                    WHEN r.id IS NULL THEN FALSE
                    ELSE TRUE
                   END) AS already_booked
                FROM reservation_time AS t
                LEFT JOIN reservation AS r
                ON t.id = r.time_id
                AND r.date = ?
                AND r.theme_id = ?
                ORDER BY convert(t.start_at, TIME) ASC;
                """;

        return jdbcTemplate.query(sql, getAvailableReservationTimeResponseRowMapper(), date, themeId);
    }

    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> createReservationTimeRowMapper() {
        return (rs, rowNum) -> {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime()
            );
        };
    }

    private RowMapper<AvailableTimeResponse> getAvailableReservationTimeResponseRowMapper() {
        return (rs, rowNum) -> {
            return new AvailableTimeResponse(
                    rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime(),
                    rs.getBoolean("already_booked")
            );
        };
    }
}

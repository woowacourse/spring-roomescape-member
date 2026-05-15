package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.exception.NotFoundException;
import roomescape.exception.ReservationAlreadyExistsException;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            LocalDate.parse(resultSet.getString("date")),
            resultSet.getLong("time_id"),
            resultSet.getLong("theme_id")
    );

    public List<Reservation> findAllReservations() {
        String sql = "SELECT * FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findReservationById(Long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public Long insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sql,
                        new String[]{"id"}
                );
                ps.setString(1, name);
                ps.setString(2, date.toString());
                ps.setLong(3, timeId);
                ps.setLong(4, themeId);
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ReservationAlreadyExistsException("해당 날짜, 시간, 테마에 대한 예약이 이미 존재입니다.");
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("해당 시간, 테마를 찾을 수 없습니다.");
        }

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Long> findReservationTimeIds(LocalDate date, Long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, date, themeId);
    }
}

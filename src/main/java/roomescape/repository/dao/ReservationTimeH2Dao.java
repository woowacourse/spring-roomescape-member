package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class ReservationTimeH2Dao implements ReservationTimeDao {

    private static final RowMapper<ReservationTime> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ReservationTime> selectAll() {
        String selectQuery = """
                SELECT id, start_at
                FROM reservation_time
                """;

        return jdbcTemplate.query(selectQuery, DEFAULT_ROW_MAPPER);
    }

    @Override
    public ReservationTime insertAndGet(ReservationTime reservationTime) {
        String insertQuery = "INSERT INTO reservation_time (start_at) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setString(1, reservationTime.startAt().toString());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, reservationTime.startAt());
    }

    @Override
    public Optional<ReservationTime> selectById(Long id) {
        String selectQuery = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id = ?
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // TODO: FK 제약 위배에 대한 예외처리 필요
    @Override
    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM reservation_time WHERE id = ?";

        try {
            jdbcTemplate.update(deleteQuery, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("삭제하려는 시간을 사용중인 예약이 있습니다.");
        }
    }

    @Override
    public List<ReservationTime> selectAllByThemeIdAndDate(Long themeId, LocalDate date) {
        String query = """
                SELECT  rt.id, rt.start_at
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                WHERE r.theme_id = ? AND r.date = ?
                """;
        return jdbcTemplate.query(query, DEFAULT_ROW_MAPPER, themeId, date);
    }
}

package roomescape.time.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.exception.ResourceInUseException;
import roomescape.time.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) ->
            new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId)
                .orElseThrow(() -> new IllegalStateException("서버 오류: 데이터 저장 직후 조회가 실패했습니다. (ID: " + generatedId + ")"));
    }

    @Override
    public void deleteById(Long id) {
        try {
            jdbcTemplate.update("delete from reservation_time where id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceInUseException("예약에 사용 중인 시간은 삭제할 수 없습니다.");
        }
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        List<ReservationTime> results = jdbcTemplate.query(sql, reservationTimeRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select exists (select 1 from reservation_time where start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("select id, start_at from reservation_time", reservationTimeRowMapper);
    }

    @Override
    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        String sql = """
            SELECT rt.id, rt.start_at
            FROM reservation_time rt
            LEFT JOIN reservation r
                ON rt.id = r.time_id
                AND r.theme_id = ?
                AND r.reservation_date = ?
            WHERE r.id IS NULL
            """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper, themeId, date);
    }
}

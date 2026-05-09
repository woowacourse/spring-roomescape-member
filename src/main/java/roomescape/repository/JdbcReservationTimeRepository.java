package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.projection.ReservationTimeEntity;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<ReservationTimeEntity> ROW_MAPPER = (rs, rowNum) -> new ReservationTimeEntity(
            rs.getLong("id"),
            new ReservationTime(rs.getTime("start_at").toLocalTime())
    );

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReservationTimeEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                ROW_MAPPER
        );
    }

    @Override
    public Optional<ReservationTimeEntity> findById(Long id) {
        List<ReservationTimeEntity> result = jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time WHERE id = ?",
                ROW_MAPPER,
                id
        );
        return result.stream().findFirst();
    }

    @Override
    public ReservationTimeEntity save(ReservationTime time) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(time.getStartAt()));
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTimeEntity(id, time);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    @Override
    public List<ReservationTimeEntity> findAvailable(LocalDate date, Long themeId) {
        String sql = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id NOT IN (
                    SELECT time_id FROM reservation
                    WHERE date = ? AND theme_id = ?
                )
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, Date.valueOf(date), themeId);
    }
}

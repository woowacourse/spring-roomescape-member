package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dao.vo.TimeRow;
import roomescape.dao.vo.TimeRows;
import roomescape.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static roomescape.dao.vo.TimeRow.ROW_MAPPER;

@Repository
public class TimeJdbcDao implements TimeDao {
    private final JdbcTemplate jdbcTemplate;

    public TimeJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insert(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO reservation_time
                (start_at)
                VALUES (?)
                """;
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"id"});
            pstmt.setString(1, time.getStartAt().toString());
            return pstmt;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Time> findById(Long id) {
        String sql = """
                SELECT * FROM reservation_time
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER, id).stream()
                .findFirst()
                .map(TimeRow::toTime);
    }

    @Override
    public List<Time> findAll() {
        String sql = """
                SELECT * FROM reservation_time
                """;
        return new TimeRows(jdbcTemplate.query(sql, ROW_MAPPER)).toTimes();
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }
}

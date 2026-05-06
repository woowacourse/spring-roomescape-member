package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Time;

@Repository
public class TimeJdbcDao implements TimeDao {
    public static final RowMapper<Time> ROW_MAPPER = (resultSet, rowNum) -> {
        return new Time(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public TimeJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Time> findAll() {
        String sql = """
                SELECT * FROM times
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Time> findById(Long id) {
        String sql = """
                SELECT * FROM times
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER, id).stream()
                .findFirst();
    }

    @Override
    public Time insert(Time time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO times
                (start_at)
                VALUES (?)
                """;
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"id"});
            pstmt.setString(1, time.getStartAt().toString());
            return pstmt;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Time(id, time.getStartAt());
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM times
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }
}

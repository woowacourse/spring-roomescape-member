package roomescape.repository.dao;

import java.sql.PreparedStatement;
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
import roomescape.domain.ReservationTheme;
import roomescape.exception.DatabaseForeignKeyException;

@Component
@RequiredArgsConstructor
public class ReservationThemeDao {

    private static final RowMapper<ReservationTheme> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new ReservationTheme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;

    public List<ReservationTheme> selectAll() {
        String selectQuery = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;

        return jdbcTemplate.query(selectQuery, DEFAULT_ROW_MAPPER);
    }

    public ReservationTheme insertAndGet(ReservationTheme reservationTheme) {
        String insertQuery = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setString(1, reservationTheme.name());
            ps.setString(2, reservationTheme.description());
            ps.setString(3, reservationTheme.thumbnail());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return reservationTheme.withId(id);
    }

    public Optional<ReservationTheme> selectById(Long id) {
        String selectQuery = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE id = ?
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM theme WHERE id = ?";

        try {
            jdbcTemplate.update(deleteQuery, id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseForeignKeyException("삭제하려는 테마에 예약이 존재합니다.", e);
        }
    }

    public List<ReservationTheme> orderByThemeBookedCountWithLimit(int limit) {
        String query = """
                SELECT th.id, th.name, th.description, th.thumbnail
                FROM reservation r
                INNER JOIN theme th ON r.theme_id = th.id
                GROUP BY r.theme_id
                ORDER BY count(r.theme_id) DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(query, DEFAULT_ROW_MAPPER, limit);
    }
}

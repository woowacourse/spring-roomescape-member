package roomescape.theme.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.SortOrder;
import roomescape.theme.domain.SortType;
import roomescape.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(String name, String description, String thumbnail) {
        String sql = "INSERT INTO themes (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, thumbnail);
            return ps;
        }, keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, name, description, thumbnail);
    }

    public void delete(long id) {
        String sql = "DELETE FROM themes WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM themes";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Theme> findRanked(SortType sortType, SortOrder sortOrder, LocalDate startDate, LocalDate endDate,
                                  Long limit) {
        String sql = getReservationSortSql(sortType, sortOrder, limit);
        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    public Optional<Theme> findById(long id) {
        String sql = "SELECT * FROM themes WHERE id = ?";

        try {
            Theme theme = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private String getReservationSortSql(SortType sortType, SortOrder sortOrder, Long limit) {
        String limitClause = limit != null ? "LIMIT " + limit : "";
        return """
                SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservationCount
                FROM themes t
                INNER JOIN reservation r ON t.id = r.theme_id
                WHERE r.date >= ? AND r.date <= ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY %s %s
                %s
                """.formatted(toColumnName(sortType), sortOrder.name(), limitClause);
    }

    private String toColumnName(SortType column) {
        return switch (column) {
            case RESERVATION_COUNT -> "reservationCount";
            case ID -> "id";
            case NAME -> "name";
        };
    }
}

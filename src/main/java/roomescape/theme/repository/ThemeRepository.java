package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.model.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new Theme(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("image_url"),
                            resultSet.getObject("required_time", LocalTime.class)
                    );
                }
        );
    }

    public Long create(Theme theme) {
        String sql = "INSERT INTO theme (name, description, image_url, required_time) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, theme.getName());
                    ps.setString(2, theme.getDescription());
                    ps.setString(3, theme.getImageUrl());
                    ps.setObject(4, theme.getRequiredTime());
                    return ps;
                }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM theme WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, image_url, required_time FROM theme WHERE id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getObject("required_time", LocalTime.class)
            ), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<PopularThemeResponse> findPopularThemes(String sort, int limit, int days) {
        String orderByColumn = "reservation_count";
        if (!"reservations".equals(sort)) {
            throw new IllegalArgumentException("예약 순으로만 정렬이 가능합니다.");
        }

        String sql = """
                SELECT t.name AS theme_name, COUNT(r.id) AS reservation_count
                FROM theme t
                INNER JOIN schedule s ON t.id = s.theme_id
                INNER JOIN reservation r ON s.id = r.schedule_id
                WHERE s.start_at >= ? AND s.start_at < ? 
                GROUP BY t.id, t.name
                ORDER BY %s DESC, t.id ASC
                LIMIT ? 
                """.formatted(orderByColumn);

        LocalDate today = LocalDate.now();
        LocalDateTime startAt = today.minusDays(days).atStartOfDay();
        LocalDateTime endAt = today.atStartOfDay();

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            return PopularThemeResponse.of(
                    resultSet.getString("theme_name"),
                    resultSet.getInt("reservation_count")
            );
        }, startAt, endAt, limit);
    }
}

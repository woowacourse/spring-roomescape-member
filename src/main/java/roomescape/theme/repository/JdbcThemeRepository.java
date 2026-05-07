package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final RowMapper<Theme> ThemeMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        String sql = """
               INSERT INTO theme (name, description, thumbnail_url)
               VALUES (?, ?, ?)
               """;

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, theme.getName());
                ps.setString(2, theme.getDescription());
                ps.setString(3, theme.getThumbnailUrl());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();

            return new Theme(
                    id,
                    theme.getName(),
                    theme.getDescription(),
                    theme.getThumbnailUrl()
            );
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("해당 테마가 이미 존재합니다.");
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = """
               DELETE FROM theme
               WHERE id = ?
               """;

        try {
            int affectedRow = jdbcTemplate.update(sql, id);

            if(affectedRow == 0) {
                throw new IllegalArgumentException("해당 id의 테마가 존재하지 않습니다.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = """
               SELECT *
               FROM theme
               WHERE id = ?
               """;

        return jdbcTemplate.query(sql, ThemeMapper, id)
                .stream().findFirst();
    }

    @Override
    public List<Theme> findAll() {
        String sql = """
                SELECT id, name, description, thumbnail_url
                FROM theme
                """;

        return jdbcTemplate.query(sql, ThemeMapper);
    }
}

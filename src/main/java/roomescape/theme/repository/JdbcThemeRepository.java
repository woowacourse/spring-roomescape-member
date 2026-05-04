package roomescape.theme.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.theme.domain.Theme;

public class JdbcThemeRepository implements ThemeRepository {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert themeInsert;

  public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.themeInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("theme")
        .usingGeneratedKeyColumns("id");
  }

  @Override
  public List<Theme> findAll() {
    return jdbcTemplate.query(
        """
        SELECT t.id, t.name, t.description, t.image_url
        FROM theme t
        """,
        new JdbcThemeRepository.ThemeRowMapper()
    );
  }

  @Override
  public Theme save(Theme theme) {
    Number id = themeInsert.executeAndReturnKey(new MapSqlParameterSource()
        .addValue("name", theme.getName())
        .addValue("description", theme.getDescription())
        .addValue("imageUrl", theme.getImageUrl()));
    return theme.withId(id.longValue());
  }

  @Override
  public boolean deleteById(long id) {
    int affectedRows = jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    return affectedRows > 0;
  }

  private static class ThemeRowMapper implements RowMapper<Theme> {

    @Override
    public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
      return null;
    }
  }
}

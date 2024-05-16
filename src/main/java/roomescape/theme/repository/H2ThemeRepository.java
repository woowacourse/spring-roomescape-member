package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;

@Repository
public class H2ThemeRepository implements ThemeRepository {

  private final NamedParameterJdbcTemplate template;

  public H2ThemeRepository(final NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public Optional<Theme> findById(final Long themeId) {
    final String sql = "SELECT * FROM theme WHERE id = :id";

    try {
      final MapSqlParameterSource param = new MapSqlParameterSource()
          .addValue("id", themeId);
      final Theme theme = template.queryForObject(sql, param, itemRowMapper());

      return Optional.of(theme);
    } catch (final EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  private RowMapper<Theme> itemRowMapper() {
    return ((rs, rowNum) -> Theme.of(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getString("description"),
        rs.getString("thumbnail")
    ));
  }

  @Override
  public List<Theme> findAll() {
    final String sql = "SELECT * FROM theme";

    return template.query(sql, itemRowMapper());
  }

  @Override
  public Theme save(final Theme theme) {
    final String sql = "INSERT INTO theme(name, description, thumbnail) VALUES(:name, :description, :thumbnail)";

    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("name", theme.getName().getValue())
        .addValue("description", theme.getDescription().getValue())
        .addValue("thumbnail", theme.getThumbnail());
    final KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder);

    final long savedThemeId = keyHolder.getKey().longValue();

    return theme.copyWithId(savedThemeId);
  }

  @Override
  public void deleteById(final Long themeId) {
    final String sql = "DELETE FROM theme WHERE id = :id";
    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", themeId);
    template.update(sql, param);
  }

  @Override
  public boolean existById(final Long themeId) {
    final String sql = "SELECT EXISTS(SELECT 1 FROM theme WHERE id = :themeId)";

    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("themeId", themeId);

    return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
  }

  @Override
  public List<Theme> findPopularThemes(final ReservationDate startAt, final ReservationDate endAt,
      final int maximumThemeCount) {
    final String sql = """
        SELECT 
        th.id, th.name, th.description, th.thumbnail 
        FROM theme as th 
        INNER JOIN reservation as r 
        on r.theme_id = th.id 
        WHERE r.date BETWEEN :startAt AND :endAt 
        GROUP BY r.theme_id 
        ORDER BY COUNT(r.theme_id) DESC 
        LIMIT :maximumThemeCount 
        """;

    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("startAt", startAt.getValue())
        .addValue("endAt", endAt.getValue())
        .addValue("maximumThemeCount", maximumThemeCount);

    return template.query(sql, param, itemRowMapper());
  }
}

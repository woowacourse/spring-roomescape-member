package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.entity.Theme;
import roomescape.mapper.ThemeMapper;

@Component
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return jdbcTemplate.query(
                sql,
                new ThemeMapper()
        );
    }

    @Override
    public Theme findById(Long id) {
        try {
            String sql = "select * from theme where id = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    new ThemeMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 테마 id입니다.");
        }
    }

    @Override
    public List<Theme> findAllById(List<Long> themeIds) {
        if (themeIds == null || themeIds.isEmpty()) {
            return Collections.emptyList();
        }

        String themeIdsToSql = themeIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));
        String sql = String.format("SELECT * FROM theme WHERE id IN (%s)", themeIdsToSql);

        List<Theme> themesById = jdbcTemplate.query(
                sql,
                themeIds.toArray(),
                new ThemeMapper()
        );

        return themeIds.stream().map(themeId ->
                        themesById.stream()
                                .filter(theme -> Objects.equals(theme.getId(), themeId))
                                .findFirst()
                                .orElseThrow(IllegalStateException::new))
                .toList();
    }

    @Override
    public Theme create(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme (name, description, thumbnail) values (?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, theme.getName());
                    ps.setString(2, theme.getDescription());
                    ps.setString(3, theme.getThumbnail());
                    return ps;
                },
                keyHolder
        );
        long themeId = keyHolder.getKey().longValue();
        return theme.copyWithId(themeId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from theme where id = ?";
        int deletedCount = jdbcTemplate.update(
                sql,
                id
        );
        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마 id입니다.");
        }
    }

    @Override
    public boolean existsByName(Theme theme) {
        String sql = "select exists (select 1 from theme where name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                theme.getName()
        ));
    }
}

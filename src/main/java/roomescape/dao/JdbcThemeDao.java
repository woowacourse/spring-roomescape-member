package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain_entity.Theme;
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
            throw new IllegalArgumentException("존재하지 않는 테마 데이터입니다.");
        }
    }

    @Override
    public Long create(Theme theme) {
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
        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(Long idRequest) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(
                sql,
                idRequest
        );
    }
}

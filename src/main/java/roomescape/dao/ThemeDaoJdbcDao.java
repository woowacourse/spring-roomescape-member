package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;

@Repository
public class ThemeDaoJdbcDao implements ThemeDao {
    public static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong(1),
                    new Name(rs.getString("name")),
                    rs.getString("thumbnail_url"),
                    rs.getString("description")
            );

    @Override
    public List<Theme> findAll() {
        return List.of();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Long insert(Theme theme) {
        return 0L;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}

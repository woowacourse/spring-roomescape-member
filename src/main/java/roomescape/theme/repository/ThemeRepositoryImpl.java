package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.PopularThemeRequestDto;
import roomescape.theme.exception.NotFoundThemeException;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );

    @Override
    public Theme add(Theme theme) {
        Long id = insertWithKeyHolder(theme);
        return findByIdOrThrow(id);
    }

    @Override
    public List<Theme> findThemesOrderByReservationCount(LocalDate from, LocalDate to,
                                                         PopularThemeRequestDto dto) { // TODO 2025. 5. 5. 14:51: AS 매번 적는 거 좀 안 좋은 것 같은데 어떻게 한번에 하는 법 없나?

        String sql0 = String.format("SELECT t.id AS id," +
                "       t.name AS name," +
                "       t.description AS description," +
                "       t.thumbnail AS thumbnail " +
                "FROM theme AS t INNER JOIN reservation AS r " +
                "ON r.theme_id = t.id " +
                "WHERE r.date BETWEEN ? AND ? " +
                "GROUP BY t.id " +
                "ORDER BY count(*) %s " +
                "LIMIT ? ", dto.sortDirection());

        return jdbcTemplate.query(sql0, themeRowMapper, from, to, dto.size());
    }

    @Override
    public Theme findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundThemeException("해당 테마 id가 존재하지 않습니다."));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, themeRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Long insertWithKeyHolder(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, themeRowMapper);
    }
}

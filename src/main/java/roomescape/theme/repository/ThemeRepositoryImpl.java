package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.globalException.CustomException;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Theme findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "해당 테마 id가 존재하지 않습니다."));
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) ->
                    new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail"))
                , id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Theme add(Theme theme) {
        Long id = insertWithKeyHolder(theme);
        return findByIdOrThrow(id);
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
}

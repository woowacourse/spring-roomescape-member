package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.globalexception.NotFoundException;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Theme add(Theme theme) {
        Long id = insertWithKeyHolder(theme);
        return findByIdOrThrow(id);
    }

    @Override
    public List<Theme> findAllOrderByRank(LocalDate from, LocalDate to, int size) {
        String sql = "SELECT t.id AS id," +
            "       t.name AS name," +
            "       t.description AS description," +
            "       t.thumbnail AS thumbnail " +
            "FROM theme AS t INNER JOIN reservation AS r " +
            "ON r.theme_id = t.id " +
            "WHERE r.date BETWEEN ? AND ? " +
            "GROUP BY t.id " +
            "ORDER BY count(*) DESC " +
            "LIMIT ? ";

        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")),
            from, to, size
        );
    }

    @Override
    public Theme findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException("해당 테마 id가 존재하지 않습니다."));
    }

    @Override
    public Optional<Theme> findById(Long id) {
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

        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
            new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"))
        );
    }
}

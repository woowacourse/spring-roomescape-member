package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;
import roomescape.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Objects;

@Repository
public class ThemeJDBCDao implements ThemeRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ThemeJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Theme findById(Long id) {
        String sql = "select * from theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedJdbcTemplate.queryForObject(sql, params, getReservationRowMapper());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into theme (name,description,thumbnail) values (:name,:description,:thumbnail)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("description", theme.description())
                .addValue("thumbnail", theme.thumbnail());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Theme(id, theme.name(), theme.description(), theme.thumbnail());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from theme where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);

        if (result == 0) {
            throw new EntityNotFoundException("테마 데이터를 찾을 수 없습니다:" + id);
        }
    }

    private RowMapper<Theme> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}

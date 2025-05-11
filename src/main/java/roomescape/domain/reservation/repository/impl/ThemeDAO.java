package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ThemeRepository;

@Repository
public class ThemeDAO implements ThemeRepository {

    private static final String TABLE_NAME = "theme";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDAO(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "select * from theme";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> themeOf(resultSet));
    }

    @Override
    public List<Theme> findThemeRankingByReservation(final LocalDate startDate, final LocalDate endDate,
                                                     final long count) {
        final String sql = """
                SELECT T.*, COUNT(R.id) AS reservation_count
                FROM theme T
                INNER JOIN reservation R ON T.id = R.theme_id
                WHERE R.date BETWEEN :start_date AND :end_date
                GROUP BY T.id
                ORDER BY reservation_count DESC
                LIMIT :count;
                """;

        final Map<String, Object> params = Map.of("start_date", startDate, "end_date", endDate, "count", count);

        return jdbcTemplate.query(sql, params, (resultSet, rowNum) -> themeOf(resultSet));
    }

    @Override
    public Theme save(final Theme theme) {
        if (theme.existId()) {
            return update(theme);
        }
        return create(theme);
    }

    @Override
    public void deleteById(final Long id) {
        final String deleteSql = "delete from theme where id = :id";
        final Map<String, Long> params = Map.of("id", id);

        final int deleteRowCount = jdbcTemplate.update(deleteSql, params);

        if (deleteRowCount != 1) {
            throw new EntityNotFoundException("Theme with id " + id + "not found");
        }
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = "select id, name, description, thumbnail from theme where id = :id";

        final Map<String, Long> params = Map.of("id", id);
        try {
            final Theme theme = jdbcTemplate.queryForObject(sql, params, (resultSet, rowNum) -> themeOf(resultSet));
            return Optional.ofNullable(theme);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Theme themeOf(final ResultSet resultSet) throws SQLException {
        return Theme.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .thumbnail(resultSet.getString("thumbnail"))
                .build();
    }

    private Theme update(final Theme theme) {
        final String sql = """
                update theme
                set name = :name, description = :description, thumbnail = :thumbnail
                where id = :id
                """;

        final Map<String, Object> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("thumbnail", theme.getThumbnail());
        params.put("id", theme.getId());

        final int updateRowCount = jdbcTemplate.update(sql, params);

        if (updateRowCount == 0) {
            throw new EntityNotFoundException("Theme with id " + theme.getId() + " not found");
        }

        return theme;
    }

    private Theme create(final Theme theme) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}

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

    public ThemeDAO(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select * from theme";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> themeOf(resultSet));
    }

    @Override
    public List<Theme> findThemeRankingByReservation(LocalDate startDate, LocalDate endDate) {
        String sql = """
                select T.*, count(R.id) as reservation_count
                from theme T
                inner join reservation R
                ON T.id = R.theme_id AND R.date between :start_date and :end_date
                group by T.id
                order by reservation_count desc
                limit 10
                """;

        Map<String, LocalDate> params = Map.of("start_date", startDate, "end_date", endDate);

        return jdbcTemplate.query(sql,
                params,
                (resultSet, rowNum) -> themeOf(resultSet));
    }

    @Override
    public Theme save(Theme theme) {
        if (theme.existId()) {
            return update(theme);
        }
        return create(theme);
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "delete from theme where id = :id";
        Map<String, Long> params = Map.of("id", id);

        int deleteRowCount = jdbcTemplate.update(deleteSql, params);

        if (deleteRowCount != 1) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, thumbnail from theme where id = :id";

        Map<String, Long> params = Map.of("id", id);
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, params,
                    (resultSet, rowNum) -> themeOf(resultSet)
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Theme with id " + id + " not found");
        }
    }

    private Theme themeOf(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    private Theme update(Theme theme) {
        String sql = """
                update theme 
                set name = :name, description = :description, thumbnail = :thumbnail
                where id = :id
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("thumbnail", theme.getThumbnail());
        params.put("id", theme.getId());

        int updateRowCount = jdbcTemplate.update(sql, params);

        if (updateRowCount == 0) {
            throw new EntityNotFoundException("ReservationTime with id " + theme.getId() + " not found");
        }

        return theme;
    }

    private Theme create(Theme theme) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}

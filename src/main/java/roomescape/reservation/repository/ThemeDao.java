package roomescape.reservation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.reservation.domain.Theme;

@Repository
public class ThemeDao {

    private static final String TABLE_NAME = "theme";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "select * from theme";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> themeOf(resultSet));
    }

    public List<Theme> findThemeRankingByReservation(final LocalDate startDate, final LocalDate endDate,
                                                     final int rowCount
    ) {
        String sql = """
                select T.*, count(R.id) as reservation_count
                from theme T
                inner join reservation R
                ON T.id = R.theme_id AND R.date between :start_date and :end_date
                group by T.id
                order by reservation_count desc
                limit :row_count
                """;

        Map<String, Object> params = Map.of("start_date", startDate, "end_date", endDate, "row_count", rowCount);

        return jdbcTemplate.query(sql,
                params,
                (resultSet, rowNum) -> themeOf(resultSet));
    }

    public Theme save(final Theme theme) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Theme.builder()
                .id(id)
                .name(theme.getName())
                .description(theme.getDescription())
                .thumbnail(theme.getThumbnail())
                .build();
    }

    public int deleteById(final Long id) {
        String deleteSql = "delete from theme where id = :id";
        Map<String, Long> params = Map.of("id", id);

        return jdbcTemplate.update(deleteSql, params);
    }

    public Optional<Theme> findById(final Long id) {
        String sql = "select id, name, description, thumbnail from theme where id = :id";

        Map<String, Long> params = Map.of("id", id);
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, params,
                    (resultSet, rowNum) -> themeOf(resultSet)
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
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
}

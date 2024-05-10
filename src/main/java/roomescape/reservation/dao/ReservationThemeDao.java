package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTheme;

@Repository
public class ReservationThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTheme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, getReservationThemeRowMapper());
    }

    public Optional<ReservationTheme> findById(Long id) {
        String sql = """
                SELECT 
                    id, 
                    name, 
                    description, 
                    thumbnail 
                FROM theme 
                WHERE id = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getReservationThemeRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public ReservationTheme insert(ReservationTheme theme) {
        String name = theme.getName();
        String description = theme.getDescription();
        String thumbnail = theme.getThumbnail();

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail);

        Long id = insertActor.executeAndReturnKey(parameters).longValue();

        return new ReservationTheme(id, name, description, thumbnail);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean isExist(Long id) {
        String sql = "SELECT EXISTS(SELECT * FROM theme WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public List<ReservationTheme> findBestThemesInWeek(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    t.id AS id,
                    COUNT(r.theme_id) AS total,
                    t.name AS name,
                    t.description AS description,
                    t.thumbnail AS thumbnail
                FROM theme AS t
                INNER JOIN reservation AS r
                ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY total DESC
                LIMIT 10
                """;
        return jdbcTemplate.query(sql, getReservationThemeRowMapper(), from.toString(), to.toString());
    }

    public Boolean hasSameName(String name) {
        String sql = "SELECT EXISTS(SELECT name FROM theme WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    private RowMapper<ReservationTheme> getReservationThemeRowMapper() {
        return (resultSet, numRow) -> new ReservationTheme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}

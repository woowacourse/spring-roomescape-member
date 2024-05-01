package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.RoomTheme;

@Repository
public class JdbcRoomThemeDao implements RoomThemeDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcRoomThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<RoomTheme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme",
                (rs, rowNum) -> new RoomTheme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ));
    }

    @Override
    public RoomTheme findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?",
                (rs, rowNum) -> new RoomTheme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ), id);
    }

    @Override
    public RoomTheme save(RoomTheme roomTheme) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", roomTheme.getName())
                .addValue("description", roomTheme.getDescription())
                .addValue("thumbnail", roomTheme.getThumbnail());

        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return roomTheme.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id) > 0;
    }
}

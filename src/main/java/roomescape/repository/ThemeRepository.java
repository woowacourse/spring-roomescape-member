package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.ThemeRequest;
import roomescape.model.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme create(ThemeRequest themeRequest) {
        String sql = "INSERT INTO theme(name, description, url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, themeRequest.getName());
            ps.setObject(2, themeRequest.getDescription());
            ps.setObject(3, themeRequest.getUrl());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        return new Theme(id, themeRequest.getName(), themeRequest.getDescription(), themeRequest.getUrl());
    }

    public void delete(long id) {
        String sql = "DELETE FROM THEME WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }
}

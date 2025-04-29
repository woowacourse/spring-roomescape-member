package roomescape.reservation.domain.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}

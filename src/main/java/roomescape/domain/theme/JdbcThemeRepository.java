package roomescape.domain.theme;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.support.exception.RoomescapeErrorCode;
import roomescape.support.exception.RoomescapeException;

@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository implements ThemeRepository {

    private static final String FIND_ALL_SQL = "select id, name, content, url from theme order by id";
    private static final String INSERT_SQL = "insert into theme(name, content, url) values (?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "delete from theme where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, themeRowMapper());
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getContent());
            ps.setString(3, theme.getUrl());
            return ps;
        }, keyHolder);
        long id = extractId(keyHolder);
        return Theme.of(
            id,
            theme.getName(),
            theme.getContent(),
            theme.getUrl()
        );
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    private RowMapper<Theme> themeRowMapper() {
        return ((rs, rowNum) -> Theme.of(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("content"),
            rs.getString("url")
        ));
    }

    private long extractId(KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new RoomescapeException(RoomescapeErrorCode.INVALID_GENERATED_KEY);
        }
        return keyHolder.getKey().longValue();
    }

}

package roomescape.theme.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.exception.ResourceInUseException;
import roomescape.theme.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final RowMapper<Theme> ThemeMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        String sql = "insert into theme (name, description, thumbnail_url) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnailUrl());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId)
                .orElseThrow(() -> new IllegalStateException("서버 오류: 데이터 저장 직후 조회가 실패했습니다. (ID: " + generatedId + ")"));
    }

    @Override
    public void deleteById(Long id) {
        try {
            jdbcTemplate.update("delete from theme where id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceInUseException("예약에 사용 중인 테마는 삭제할 수 없습니다.");
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";
        List<Theme> results = jdbcTemplate.query(sql, ThemeMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("select id, name, description, thumbnail_url from theme order by id", ThemeMapper);
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select exists (select 1 from theme where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    @Override
    public List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
            select
                t.id,
                t.name,
                t.description,
                t.thumbnail_url
            from reservation r
            inner join theme t on r.theme_id = t.id
            where r.reservation_date >= ?
            and r.reservation_date < ?
            group by t.id
            order by count(r.id) desc, t.id asc
            limit ?
            """;

        return jdbcTemplate.query(
                sql,
                ThemeMapper,
                Date.valueOf(startDate),
                Date.valueOf(endDate),
                limit
        );
    }
}

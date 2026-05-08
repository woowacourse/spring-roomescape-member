package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcThemeSlotRepository implements ThemeSlotRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeSlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ThemeSlot save(ThemeSlot themeSlot) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(themeSlot);
        long themeSlotId = insert.executeAndReturnKey(params).longValue();
        return ThemeSlot.of(themeSlotId, themeSlot);
    }

    @Override
    public List<ThemeSlot> saveAll(List<ThemeSlot> themeSlots) {
        List<ThemeSlot> results = new ArrayList<>();
        for (ThemeSlot themeSlot : themeSlots) {
            SimpleJdbcInsert insert = createInsert();
            Map<String, Object> params = createParams(themeSlot);
            long themeSlotId = insert.executeAndReturnKey(params).longValue();
            results.add(ThemeSlot.of(themeSlotId, themeSlot));
        }
        return results;
    }

    @Override
    public List<ThemeSlot> findAll() {
        String sql = """
                SELECT 
                    ts.id AS id,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail_url AS theme_thumbnail_url,
                    ts.date AS date,
                    t.id AS time_id, 
                    t.start_at AS start_at,
                    ts.is_reserved AS is_reserved
                FROM 
                    theme_slot ts
                        INNER JOIN time t ON ts.time_id = t.id 
                        INNER JOIN theme th ON ts.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Optional<ThemeSlot> findById(long reservationId) {
        String sql = """
                SELECT 
                    r.id AS r_id,
                    r.name,
                    r.date,
                    t.id AS t_id,
                    t.start_at, 
                    theme.id as theme_id,
                    theme.name AS theme_name,
                    theme.description AS theme_description,
                    theme.thumbnail_url AS theme_thumbnail_url
                FROM 
                    reservation r 
                        INNER JOIN 
                        time t 
                        INNER JOIN 
                        theme theme
                            ON 
                                r.time_id = t.id 
                                   AND 
                                r.theme_id = theme.id
                WHERE r.id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), reservationId));
    }

    @Override
    public boolean isExistBy(long themeId, LocalDate date) {
        String sql = """
                        SELECT EXISTS (
                            SELECT 1
                            FROM theme_slot 
                            WHERE theme_id = ? 
                            AND date = ?
                        ) 
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId, date));
    }

    @Override
    public List<ThemeSlot> findByThemeIdAndDate(long themeId, LocalDate date) {
        String sql = """
                SELECT 
                    ts.id AS id,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail_url AS theme_thumbnail_url,
                    ts.date AS date,
                    t.id AS time_id, 
                    t.start_at AS start_at,
                    ts.is_reserved AS is_reserved
                FROM 
                    theme_slot ts
                        INNER JOIN time t ON ts.time_id = t.id 
                        INNER JOIN theme th ON ts.theme_id = th.id
                WHERE ts.theme_id = ?
                AND ts.date = ?
                """;
        return jdbcTemplate.query(sql, rowMapper(), themeId, date);
    }

    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme_slot")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(ThemeSlot themeSlot) {
        return Map.of(
                "theme_id", themeSlot.getTheme().getId(),
                "date", themeSlot.getDate(),
                "time_id", themeSlot.getTime().getId(),
                "is_reserved", themeSlot.isReserved()
        );
    }

    @Override
    public void deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    @Override
    public void update(ThemeSlot themeSlot) {
        String sql = """
                UPDATE theme_slot 
                SET is_reserved = ? 
                WHERE theme_id = ?
                AND date = ?
                AND time_id = ?
                """;

        jdbcTemplate.update(sql,
                themeSlot.isReserved(),
                themeSlot.getTheme().getId(),
                themeSlot.getDate(),
                themeSlot.getTime().getId()
        );
    }

    private RowMapper<ThemeSlot> rowMapper() {
        return (rs, rowNum) -> new ThemeSlot(
                rs.getLong("id"),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("theme_description"),
                        rs.getString("theme_thumbnail_url")
                ),
                rs.getObject("date", LocalDate.class),
                new Time(
                        rs.getLong("time_id"),
                        rs.getObject("start_at", LocalTime.class)),
                rs.getBoolean("is_reserved")
        );
    }
}

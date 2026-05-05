package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme.PopularThemeCondition;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.domain.ReservationTheme.ReservationThemeDaoData;
import roomescape.domain.ReservationTheme.ReservationThemeWithCountDaoData;

@Repository
public class ReservationThemeDao {
    private static final String TABLE_NAME = "reservation_theme";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    private static final String SELECT_ALL_SQL = "SELECT id, name, description, image_url FROM reservation_theme";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_theme WHERE id = ?";
    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, name, description, image_url FROM reservation_theme WHERE id = ?";

    private static final String SELECT_POPULAR_THEMES_BY_DATE_RANGE = """
        SELECT t.id AS id, t.name AS name, t.description AS description, t.image_url AS image_url, COUNT(r.id) AS count \s
        FROM reservation_theme t \s
        JOIN ( \s
            SELECT theme_id, COUNT(id) AS reservation_count \s
            FROM reservation \s
            WHERE created_at BETWEEN ? AND ? \s
            GROUP BY theme_id \s
        ) AS r ON r.theme_id = t.id \s
        ORDER BY r.reservation_count ? \s
        LIMIT ?
    """;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public ReservationThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID);
    }

    public ReservationThemeDaoData addTheme(ReservationThemeCommand reservationThemeCommand) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_NAME, reservationThemeCommand.name(),
                COLUMN_DESCRIPTION, reservationThemeCommand.description(),
                COLUMN_IMAGE_URL, reservationThemeCommand.imageUrl()
        )).longValue();

        return ReservationThemeDaoData.from(id, reservationThemeCommand);
    }

    public List<ReservationThemeDaoData> getAllTheme() {
        return jdbcTemplate.query(SELECT_ALL_SQL, (rs, i) -> ReservationThemeDaoData.from(rs));
    }

    public void deleteTheme(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    public Optional<ReservationThemeDaoData> getTheme(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, ((rs, rowNum) -> ReservationThemeDaoData.from(rs)), id)
                .stream()
                .findFirst();
    }

    public List<ReservationThemeWithCountDaoData> getPopularTheme(PopularThemeCondition popularThemeCondition) {
        return jdbcTemplate.query(SELECT_ALL_SQL, (rs, i) -> ReservationThemeWithCountDaoData.from(rs),
                popularThemeCondition.startDate(),
                popularThemeCondition.endDate(),
                popularThemeCondition.order(),
                popularThemeCondition.size()
        );
    }
}

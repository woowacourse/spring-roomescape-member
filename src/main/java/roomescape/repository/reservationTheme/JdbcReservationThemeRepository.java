package roomescape.repository.reservationTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.QueryWithParams;
import roomescape.domain.reservationTheme.PopularThemeCondition;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.domain.reservationTheme.ReservationThemeCommand;
import roomescape.domain.reservationTheme.ReservationThemeWithCount;

@Repository
public class JdbcReservationThemeRepository implements ReservationThemeRepository {
    private static final String TABLE_NAME = "reservation_theme";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    private static final String SELECT_ALL_SQL = "SELECT id, name, description, image_url FROM reservation_theme";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_theme WHERE id = ?";
    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, name, description, image_url FROM reservation_theme WHERE id = ?";

    private static final RowMapper<ReservationTheme> MAPPER = (rs, rowNum) -> new ReservationTheme(
            rs.getLong(COLUMN_ID),
            rs.getString(COLUMN_NAME),
            rs.getString(COLUMN_DESCRIPTION),
            rs.getString(COLUMN_IMAGE_URL)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID);    }

    public ReservationTheme addTheme(ReservationThemeCommand reservationThemeCommand) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_NAME, reservationThemeCommand.name(),
                COLUMN_DESCRIPTION, reservationThemeCommand.description(),
                COLUMN_IMAGE_URL, reservationThemeCommand.imageUrl()
        )).longValue();

        return ReservationTheme.from(id, reservationThemeCommand);
    }

    public List<ReservationTheme> getAllTheme() {
        return jdbcTemplate.query(SELECT_ALL_SQL, MAPPER);
    }

    public Optional<ReservationTheme> getTheme(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, MAPPER, id)
                .stream()
                .findFirst();
    }

    public void deleteTheme(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    @Override
    public List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
        QueryWithParams queryWithParams = getPopularThemQuery(popularThemeCondition);
        return jdbcTemplate.query(
                queryWithParams.query(),
                (rs, i) -> ReservationThemeWithCount.from(rs),
                queryWithParams.params().toArray()
        );
    }

    private QueryWithParams getPopularThemQuery(PopularThemeCondition popularThemeCondition) {
        StringBuilder query = new StringBuilder("""
          SELECT t.id, t.name, t.description, t.image_url, r.reservation_count AS count
          FROM reservation_theme t
          JOIN (
              SELECT theme_id, COUNT(id) AS reservation_count
              FROM reservation
        """);

        List<String> condition = new ArrayList<>();
        List<String> params = new ArrayList<>();

        if(popularThemeCondition.startDate() != null) {
            condition.add("created_at >= ?");
            params.add(popularThemeCondition.startDate());
        }

        if(popularThemeCondition.endDate() != null) {
            condition.add("created_at <= ?");
            params.add(popularThemeCondition.endDate());
        }

        if(!condition.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", condition));
        }

        query.append("""
               GROUP BY theme_id
          ) AS r ON r.theme_id = t.id
          ORDER BY r.reservation_count DESC
          LIMIT ?
        """);

        params.add(String.valueOf(popularThemeCondition.size()));

        return new QueryWithParams(query.toString(), params);
    }
}

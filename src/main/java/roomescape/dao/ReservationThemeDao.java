package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme.ReservationThemeCommand;
import roomescape.domain.ReservationTheme.ReservationThemeDaoData;

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
}

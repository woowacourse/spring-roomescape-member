package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_BASE_SQL = """
            SELECT r.id, r.name, r.date,
                   t.id AS time_id, t.start_at AS time_value,
                   th.id AS theme_id, th.name AS theme_name,
                   th.description AS theme_description, th.thumbnail_url AS theme_thumbnail_url
            FROM `reservation` r
            INNER JOIN `reservation_time` t ON r.time_id = t.id
            INNER JOIN `theme` th ON r.theme_id = th.id""";

    private static final String CREATE_SQL =
            "INSERT INTO `reservation`(`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = SELECT_RESERVATION_BASE_SQL;
    private static final String FIND_BY_ID_SQL = SELECT_RESERVATION_BASE_SQL + " WHERE r.id = ?";
    private static final String FIND_ALL_BY_NAME_SQL = SELECT_RESERVATION_BASE_SQL + " WHERE r.name = ?";
    private static final String UPDATE_SQL =
            "UPDATE `reservation` SET `date` = ?, `time_id` = ? WHERE `id` = ?";
    private static final String DELETE_SQL =
            "DELETE FROM `reservation` WHERE `id` = ?";
    private static final String EXISTS_BY_DATE_AND_TIME_AND_THEME_SQL =
            "SELECT EXISTS (SELECT 1 FROM `reservation` WHERE `date` = ? AND `time_id` = ? AND `theme_id` = ?)";
    private static final String EXISTS_BY_DATE_AND_TIME_AND_THEME_EXCLUDING_ID_SQL =
            "SELECT EXISTS (SELECT 1 FROM `reservation` WHERE `date` = ? AND `time_id` = ? AND `theme_id` = ? AND `id` <> ?)";
    private static final String EXISTS_BY_TIME_ID_SQL =
            "SELECT EXISTS (SELECT 1 FROM `reservation` WHERE `time_id` = ?)";
    private static final String EXISTS_BY_THEME_ID_SQL =
            "SELECT EXISTS (SELECT 1 FROM `reservation` WHERE `theme_id` = ?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation create(Reservation reservationWithoutId) {
        ReservationTime time = reservationWithoutId.getTime();
        Theme theme = reservationWithoutId.getTheme();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, new String[]{"id"});
            preparedStatement.setString(1, reservationWithoutId.getName());
            preparedStatement.setDate(2, Date.valueOf(reservationWithoutId.getDate()));
            preparedStatement.setLong(3, time.getId());
            preparedStatement.setLong(4, theme.getId());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return Reservation.of(id, reservationWithoutId);
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, this::mapToReservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jdbcTemplate
                .query(FIND_BY_ID_SQL, this::mapToReservation, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAllByName(String name) {
        return jdbcTemplate.query(FIND_ALL_BY_NAME_SQL, this::mapToReservation, name);
    }

    @Override
    public void update(Reservation reservation) {
        jdbcTemplate.update(UPDATE_SQL,
                Date.valueOf(reservation.getDate()),
                reservation.getTime().getId(),
                reservation.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_DATE_AND_TIME_AND_THEME_SQL, Boolean.class,
                        date, timeId, themeId));
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeIdExcludingId(LocalDate date, Long timeId, Long themeId,
                                                              Long excludeId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_DATE_AND_TIME_AND_THEME_EXCLUDING_ID_SQL, Boolean.class,
                        date, timeId, themeId, excludeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_TIME_ID_SQL, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(Long timeId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_THEME_ID_SQL, Boolean.class, timeId));
    }

    private Reservation mapToReservation(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        Long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getTime("time_value").toLocalTime();
        Long themeId = resultSet.getLong("theme_id");
        String themeName = resultSet.getString("theme_name");
        String themeDescription = resultSet.getString("theme_description");
        String themeThumbnailUrl = resultSet.getString("theme_thumbnail_url");

        ReservationTime reservationTime = new ReservationTime(timeId, timeValue);
        Theme theme = new Theme(themeId, themeName, themeDescription, themeThumbnailUrl);
        return new Reservation(id, name, date, reservationTime, theme);
    }
}

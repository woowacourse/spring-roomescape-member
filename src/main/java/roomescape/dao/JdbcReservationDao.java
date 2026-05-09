package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Primary
@Repository
public class JdbcReservationDao implements ReservationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation create(Reservation reservationWithoutId, ReservationTime reservationTime, Theme theme) {
        String sql = "INSERT INTO `reservation`(`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservationWithoutId.getName());
            preparedStatement.setDate(2, Date.valueOf(reservationWithoutId.getDate()));
            preparedStatement.setLong(3, reservationTime.getId());
            preparedStatement.setLong(4, theme.getId());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return Reservation.of(id, reservationWithoutId);
    }

    @Override
    public List<Reservation> readAll() {
        String sql =
                "SELECT r.id, r.name, r.date, t.id as time_id, t.start_at as time_value, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail_url as theme_thumbnail_url "
                        + "FROM `reservation` r "
                        + "INNER JOIN `reservation_time` t ON r.time_id = t.id "
                        + "INNER JOIN `theme` th ON r.theme_id = th.id";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
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
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM `reservation` WHERE `id` = (?)";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS ("
                + "SELECT 1 FROM `reservation` WHERE `date` = (?) AND `time_id` = (?) AND `theme_id` = (?)"
                + ") AS exist";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }
}

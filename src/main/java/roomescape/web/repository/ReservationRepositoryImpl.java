package roomescape.web.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.domain.Theme;
import roomescape.core.dto.BookingTimeResponseDto;
import roomescape.core.repository.ReservationRepository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        final String query = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    m.id AS theme_id,
                    m.name AS theme_name,
                    m.description AS theme_description,
                    m.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS m
                ON r.theme_id = m.id
                """;
        return jdbcTemplate.query(query, getReservationRowMapper());
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String date = resultSet.getString("date");
            final Long tId = resultSet.getLong("time_id");
            final String timeValue = resultSet.getString("time_value");
            final ReservationTime time = new ReservationTime(tId, timeValue);
            final Long mId = resultSet.getLong("theme_id");
            final String themeName = resultSet.getString("theme_name");
            final String themeDescription = resultSet.getString("theme_description");
            final String themeThumbnail = resultSet.getString("theme_thumbnail");
            final Theme theme = new Theme(mId, themeName, themeDescription, themeThumbnail);

            return new Reservation(id, name, date, time, theme);
        };
    }

    @Override
    public List<BookingTimeResponseDto> findAllByDateNotOrThemeIdNot(final String date, final long themeId) {
        final String query = """
                SELECT t.id, t.start_at, r.id IS NOT NULL AS already_booked
                FROM reservation_time AS t 
                LEFT JOIN (
                    SELECT * FROM reservation WHERE date LIKE ? AND theme_id = ?
                ) AS r
                ON t.id = r.time_id;
                """;
        return jdbcTemplate.query(query, getBookingTimeRowMapper(), date, themeId);
    }

    private RowMapper<BookingTimeResponseDto> getBookingTimeRowMapper() {
        return (resultSet, rowNum) -> {
            final Long timeId = resultSet.getLong("id");
            final String timeValue = resultSet.getString("start_at");
            final boolean alreadyBooked = resultSet.getBoolean("already_booked");

            return new BookingTimeResponseDto(timeId, timeValue, alreadyBooked);
        };
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(String date, long themeId) {
        final String query = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    m.id AS theme_id,
                    m.name AS theme_name,
                    m.description AS theme_description,
                    m.thumbnail AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS m
                ON r.theme_id = m.id
                WHERE r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(query, getReservationRowMapper(), date, themeId);
    }

    @Override
    public boolean existByTimeId(final long timeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean existByThemeId(final long themeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(final String date, final long timeId, final long themeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId));
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}

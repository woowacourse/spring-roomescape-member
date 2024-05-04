package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ReservationRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT R.ID, R.NAME, R.DATE, R.TIME_ID, R.THEME_ID, RT.START_AT, T.NAME, T.NAME, T.DESCRIPTION, T.THUMBNAIL
                FROM RESERVATION AS R
                LEFT JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                LEFT JOIN THEME T ON T.ID = R.THEME_ID
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Reservation(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getDate("DATE").toLocalDate(),
                new ReservationTime(
                        rs.getLong("RESERVATION.ID"),
                        rs.getTime("RESERVATION_TIME.START_AT").toLocalTime()
                ),
                new Theme(
                        rs.getLong("RESERVATION.ID"),
                        rs.getString("THEME.NAME"),
                        rs.getString("THEME.DESCRIPTION"),
                        rs.getString("THEME.THUMBNAIL")
                )))
        );
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT R.ID, R.NAME, R.DATE, R.TIME_ID, R.THEME_ID, RT.START_AT, T.NAME FROM RESERVATION AS R
                JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                JOIN THEME T ON T.ID = R.THEME_ID
                WHERE R.DATE = ? AND T.ID = ?
                """;

        //TODO 메서드 분리
        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Reservation(
                        rs.getLong("ID"),
                        rs.getString("NAME"),
                        rs.getDate("DATE").toLocalDate(),
                        new ReservationTime(
                                rs.getLong("TIME_ID"),
                                rs.getTime("START_AT").toLocalTime()),
                        new Theme(
                                rs.getLong("RESERVATION.THEME_ID"),
                                null,
                                null,
                                null))),
                date, themeId);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = "SELECT * FROM reservation WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapRowReservation, id)
                .stream()
                .findAny();
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        final String sql = "SELECT * FROM RESERVATION WHERE TIME_ID = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, timeId)
                .isEmpty();
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        final String sql = "SELECT * FROM RESERVATION WHERE THEME_ID = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, themeId)
                .isEmpty();
    }

    private Reservation mapRowReservation(final ResultSet rs, final int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getDate("DATE").toLocalDate(),
                new ReservationTime(rs.getLong("RESERVATION.TIME_ID"), null),
                new Theme(rs.getLong("RESERVATION.THEME_ID"), null, null, null)
        );
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("NAME", reservation.getName())
                .addValue("DATE", reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addValue("TIME_ID", reservation.getTime().getId())
                .addValue("THEME_ID", reservation.getTheme().getId());

        final Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
    }

    @Override
    public boolean existsByThemesAndDateAndTimeId(final Long themeId, final Long timeId, final LocalDate date) {
        final String sql = "SELECT * FROM RESERVATION WHERE THEME_ID = ? AND TIME_ID = ? AND DATE = ? LIMIT 1";
        return !jdbcTemplate.query(sql, this::mapRowReservation, themeId, timeId, date).isEmpty();
    }

    @Override
    public int deleteById(final Long id) {
        final String sql = "DELETE FROM RESERVATION WHERE ID = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemes(final LocalDate today) {
        final LocalDate popularRangeStart = today.minusDays(7);
        final LocalDate popularRangeEnd = today.minusDays(1);

        final String sql = """
                SELECT R.THEME_ID, T.NAME, T.THUMBNAIL, T.DESCRIPTION, COUNT(T.ID) AS COUNT FROM RESERVATION AS R
                JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                JOIN THEME T ON T.ID = R.THEME_ID
                WHERE R.DATE BETWEEN  ? AND ?
                GROUP BY R.THEME_ID
                ORDER BY COUNT DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Theme(
                rs.getLong("THEME_ID"),
                rs.getString("THEME.NAME"),
                rs.getString("THEME.DESCRIPTION"),
                rs.getString("THEME.THUMBNAIL")
        )), popularRangeStart, popularRangeEnd);
    }
}

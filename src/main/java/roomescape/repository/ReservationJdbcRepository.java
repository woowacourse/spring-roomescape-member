package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcRepository(final DataSource dataSource) {
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

        return jdbcTemplate.query(sql, createReservationRowMapper());
    }

    @Override
    public List<Reservation> findAllByTimeSlotId(final Long id) {
        final String sql = """
                SELECT R.ID, R.NAME, R.DATE, R.TIME_ID, R.THEME_ID, RT.START_AT, T.NAME, T.NAME, T.DESCRIPTION, T.THUMBNAIL
                FROM RESERVATION AS R
                LEFT JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                LEFT JOIN THEME T ON T.ID = R.THEME_ID
                WHERE RT.ID = ?
                """;

        return jdbcTemplate.query(sql, createReservationRowMapper(), id);
    }

    @Override
    public List<Reservation> findAllByThemeId(final Long id) {
        final String sql = """
                SELECT R.ID, R.NAME, R.DATE, R.TIME_ID, R.THEME_ID, RT.START_AT, T.NAME, T.NAME, T.DESCRIPTION, T.THUMBNAIL
                FROM RESERVATION AS R
                LEFT JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                LEFT JOIN THEME T ON T.ID = R.THEME_ID
                WHERE T.ID = ?
                """;

        return jdbcTemplate.query(sql, createReservationRowMapper(), id);
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT R.ID, R.NAME, R.DATE, R.TIME_ID, R.THEME_ID, RT.START_AT, T.NAME, T.DESCRIPTION, T.THUMBNAIL FROM RESERVATION AS R
                JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                JOIN THEME T ON T.ID = R.THEME_ID
                WHERE R.DATE = ? AND T.ID = ?
                """;

        return jdbcTemplate.query(sql, createReservationRowMapper(), date, themeId);
    }

    @Override
    public List<Theme> findPopularThemesByPeriod(final LocalDate startDate, final LocalDate endDate,
                                                 final Integer limit) {
        final String sql = """
                SELECT R.THEME_ID, T.NAME, T.THUMBNAIL, T.DESCRIPTION, COUNT(T.ID) AS COUNT FROM RESERVATION AS R
                JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                JOIN THEME T ON T.ID = R.THEME_ID
                WHERE R.DATE BETWEEN  ? AND ?
                GROUP BY R.THEME_ID
                ORDER BY COUNT DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Theme(
                rs.getLong("THEME_ID"),
                rs.getString("THEME.NAME"),
                rs.getString("THEME.DESCRIPTION"),
                rs.getString("THEME.THUMBNAIL")
        )), startDate, endDate, limit);
    }

    @Override
    public Long save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("NAME", reservation.name())
                .addValue("DATE", reservation.date())
                .addValue("TIME_ID", reservation.timeSlot().id())
                .addValue("THEME_ID", reservation.theme().id());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = "SELECT * FROM RESERVATION WHERE ID = ?";

        return jdbcTemplate.query(sql, createSimpleReservationRowMapper(), id)
                .stream()
                .findAny();
    }

    @Override
    public Boolean removeById(final Long id) {
        final String sql = "DELETE FROM RESERVATION WHERE ID = ?";

        int removedRowsCount = jdbcTemplate.update(sql, id);
        return removedRowsCount > 0;
    }

    private RowMapper<Reservation> createReservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getDate("DATE").toLocalDate(),
                new TimeSlot(
                        rs.getLong("RESERVATION.TIME_ID"),
                        rs.getTime("RESERVATION_TIME.START_AT").toLocalTime()
                ),
                new Theme(
                        rs.getLong("RESERVATION.THEME_ID"),
                        rs.getString("THEME.NAME"),
                        rs.getString("THEME.DESCRIPTION"),
                        rs.getString("THEME.THUMBNAIL")
                ));
    }

    private RowMapper<Reservation> createSimpleReservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getDate("DATE").toLocalDate(),
                new TimeSlot(rs.getLong("RESERVATION.TIME_ID"), null),
                new Theme(rs.getLong("RESERVATION.THEME_ID"), null, null, null)
        );
    }
}

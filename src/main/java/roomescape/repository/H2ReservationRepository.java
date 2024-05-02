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
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private Reservation mapRowReservation(final ResultSet rs, final int rowNum) throws SQLException {
        return Reservation.from(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                new ReservationTime(rs.getLong("time_id"), "00:00"),
                new Theme(rs.getLong("theme_id"), null, null, null)
        );
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, t.name FROM reservation AS R
                LEFT JOIN reservation_time RT on RT.ID = R.TIME_ID
                LEFT JOIN THEME T on T.ID = R.THEME_ID
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> Reservation.from(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getString("start_at")),
                new Theme(rs.getLong("theme_id"), null, null, null))));
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, t.name FROM reservation AS R
                JOIN reservation_time RT on RT.id = R.time_id
                JOIN theme T on T.id = R.theme_id
                where R.date = ? and T.id = ?
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> Reservation.from(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        new ReservationTime(
                                rs.getLong("time_id"),
                                rs.getString("start_at")),
                        new Theme(rs.getLong("theme_id"), null, null, null))),
                date, themeId);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = "SELECT * FROM reservation WHERE id = ?";

        return jdbcTemplate.query(sql, this::mapRowReservation, id)
                .stream()
                .findAny();
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        final String sql = "SELECT * FROM reservation WHERE time_id = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, timeId)
                .isEmpty();
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        final String sql = "SELECT * FROM reservation WHERE theme_id = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, themeId)
                .isEmpty();
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        final Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    @Override
    public boolean existsByDateAndTimeId(final Long timeId, final LocalDate date) {
        final String sql = "SELECT * FROM reservation WHERE time_id = ? AND date = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowReservation, timeId, date).isEmpty();
    }

    @Override
    public int deleteById(final Long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemes(final LocalDate today) {
        //TODO 이름 고치기
        final LocalDate localDate1 = today.minusDays(7);
        final LocalDate localDate2 = today.minusDays(1);

        final String sql = """
                SELECT r.theme_id, t.name, t.THUMBNAIL, t.DESCRIPTION, count(t.ID) as count FROM reservation AS R
                JOIN reservation_time RT on RT.ID = R.TIME_ID
                JOIN THEME T on T.ID = R.THEME_ID
                WHERE R.DATE BETWEEN  ? AND ?
                GROUP BY (r.theme_id)
                ORDER BY count desc
                """;

        return jdbcTemplate.query(sql, ((rs, rowNum) -> new Theme(
                rs.getLong("reservation.theme_id"),
                rs.getString("theme.name"),
                rs.getString("theme.description"),
                rs.getString("theme.thumbnail")
        )), localDate1, localDate2);
    }
}

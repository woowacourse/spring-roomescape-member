package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.*;

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

    public H2ReservationRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
    }

    private Reservation mapRowLazy(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("ID"),
                rs.getString("DATE"),
                rs.getLong("TIME_ID"),
                rs.getLong("THEME_ID"),
                rs.getLong("MEMBER_ID")
        );
    }

    private Reservation mapRowTime(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("RESERVATION.ID"),
                rs.getString("RESERVATION.DATE"),
                new ReservationTime(
                        rs.getLong("RESERVATION_TIME.ID"),
                        rs.getString("RESERVATION_TIME.START_AT")),
                new Theme(rs.getLong("RESERVATION.THEME_ID")),
                new Member(rs.getLong("RESERVATION.MEMBER_ID"))
        );
    }

    private Reservation mapRowFull(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("RESERVATION.ID"),
                rs.getString("RESERVATION.DATE"),
                new ReservationTime(
                        rs.getLong("RESERVATION_TIME.ID"),
                        rs.getString("RESERVATION_TIME.START_AT")),
                new Theme(
                        rs.getLong("THEME.ID"),
                        rs.getString("THEME.NAME"),
                        rs.getString("THEME.DESCRIPTION"),
                        rs.getString("THEME.THUMBNAIL")),
                new Member(
                        rs.getLong("MEMBER.ID"),
                        rs.getString("MEMBER.NAME"),
                        rs.getString("MEMBER.EMAIL"),
                        rs.getString("MEMBER.PASSWORD"),
                        Role.valueOf(rs.getString("MEMBER.ROLE"))
                )
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT * FROM RESERVATION AS R
                LEFT JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                LEFT JOIN THEME T ON T.ID = R.THEME_ID
                LEFT JOIN MEMBER M ON M.ID = R.MEMBER_ID
                """;

        return jdbcTemplate.query(sql, this::mapRowFull);
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, long themeId) {
        String sql = """
                SELECT * FROM RESERVATION AS R
                INNER JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                WHERE R.DATE = ? AND R.THEME_ID = ?
                """;

        return jdbcTemplate.query(sql, this::mapRowTime, date, themeId);
    }

    @Override
    public List<Reservation> findAllByThemeIdAndMemberIdAndDateRange(Long themeId, Long memberId, String dateFrom, String dateTo) {
        String sql = """
                SELECT * FROM RESERVATION AS R
                LEFT JOIN RESERVATION_TIME RT ON RT.ID = R.TIME_ID
                LEFT JOIN THEME T ON T.ID = R.THEME_ID
                LEFT JOIN MEMBER M ON M.ID = R.MEMBER_ID
                WHERE T.ID = ? AND M.ID = ?
                    AND R.DATE BETWEEN ? AND ?
                """;

        return jdbcTemplate.query(sql, this::mapRowFull, themeId, memberId, dateFrom, dateTo);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";

        return jdbcTemplate.query(sql, this::mapRowLazy, id)
                .stream()
                .findAny();
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String sql = "SELECT * FROM RESERVATION WHERE TIME_ID = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowLazy, timeId)
                .isEmpty();
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        String sql = "SELECT * FROM RESERVATION WHERE THEME_ID = ? LIMIT 1";

        return !jdbcTemplate.query(sql, this::mapRowLazy, themeId)
                .isEmpty();
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT * FROM RESERVATION
                WHERE DATE = ? AND TIME_ID = ? AND THEME_ID = ?
                LIMIT 1
                """;

        return !jdbcTemplate.query(sql, this::mapRowLazy, date, timeId, themeId)
                .isEmpty();
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("DATE", reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .addValue("TIME_ID", reservation.getTime().getId())
                .addValue("THEME_ID", reservation.getTheme().getId())
                .addValue("MEMBER_ID", reservation.getMember().getId());

        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    @Override
    public int delete(long id) {
        String sql = "DELETE FROM RESERVATION WHERE ID = ?";

        return jdbcTemplate.update(sql, id);
    }
}

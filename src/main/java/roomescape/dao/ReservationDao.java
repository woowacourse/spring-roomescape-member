package roomescape.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.ReservationRowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper rowMapper;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource, ReservationRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Reservation create(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getNameAsString())
                .addValue("date", reservation.getDate().asString())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public boolean isExistByReservationAndTime(ReservationDate date, long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date.asString(), timeId));
    }

    public boolean isExistByTimeId(long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    public boolean isExistByThemeId(long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId));
    }

    public boolean isExistById(long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public List<Reservation> getAll() {
        String sql = """
                SELECT
                r.id AS reservation_id,
                r.name,
                r.date,
                t.id AS time_id,
                t.start_at AS time_value
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

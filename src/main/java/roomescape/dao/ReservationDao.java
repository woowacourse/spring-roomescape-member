package roomescape.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.ReservationRowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.exception.CustomException2;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper rowMapper;

    public ReservationDao(final JdbcTemplate jdbcTemplate,
                          final DataSource dataSource,
                          final ReservationRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Reservation create(final Reservation reservation) {
        final var params = new MapSqlParameterSource()
                .addValue("date", reservation.getDateAsString())
                .addValue("time_id", reservation.getTime().id())
                .addValue("theme_id", reservation.getTheme().id())
                .addValue("member_id", reservation.getMember().id());
        try {
            final var id = jdbcInsert.executeAndReturnKey(params).longValue();
            return Reservation.of(id, reservation);
        } catch (final DuplicateKeyException error) {
            throw new CustomException2("중복 예약은 불가능합니다.");
        }
    }

    public boolean isExistByReservationAndTime(final ReservationDate date, final long timeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date.asString(), timeId));
    }

    public boolean isExistByTimeId(final long timeId) {
        return isExistByCondition("time_id", timeId);
    }

    public boolean isExistByThemeId(final long themeId) {
        return isExistByCondition("theme_id", themeId);
    }

    public boolean isExistById(final long id) {
        return isExistByCondition("id", id);
    }

    private boolean isExistByCondition(final String conditionColumn, final Object conditionValue) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE " + conditionColumn + " = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, conditionValue));
    }

    public List<Reservation> getAll() {
        final String sql = """
                SELECT
                r.id AS reservation_id,
                r.date,
                t.id AS time_id,
                t.start_at,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.name AS member_name,
                m.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id 
                INNER JOIN theme AS th 
                ON r.theme_id = th.id 
                INNER JOIN member AS m
                ON r.member_id = m.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

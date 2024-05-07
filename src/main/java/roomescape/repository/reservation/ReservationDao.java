package roomescape.repository.reservation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private static RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        return new Reservation(id, reservation);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id""";
        return jdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        try {
            String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                WHERE r.id = ?""";
            Reservation reservation = jdbcTemplate.queryForObject(sql, getReservationRowMapper(), id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByDateBetween(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                WHERE r.date BETWEEN ? AND ?""";
        return jdbcTemplate.query(sql, getReservationRowMapper(), start, end);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                WHERE r.date = ? AND th.id = ?""";
        return jdbcTemplate.query(sql, getReservationRowMapper(), date, themeId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT id FROM reservation WHERE time_id = ? LIMIT 1";
        List<Long> ids = jdbcTemplate.query(
                sql, (rs, rowNum) -> rs.getLong("id"), timeId
        );
        return !ids.isEmpty();
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT id FROM reservation WHERE theme_id = ? LIMIT 1";
        List<Long> ids = jdbcTemplate.query(
                sql, (rs, rowNum) -> rs.getLong("id"), themeId
        );
        return !ids.isEmpty();
    }

    @Override
    public boolean existsBy(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(isExist);
    }
}

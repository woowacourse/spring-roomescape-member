package roomescape.domain.reservation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("reservation")
            .usingColumns("name", "date", "time_id", "theme_id")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """
                SELECT r.id, r.name, r.date, rt.id AS time_id, rt.start_at, t.id AS theme_id, t.name AS theme_name, t.description, t.image_url
                FROM reservation r
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, this::mapReservation);
    }

    @Override
    public Optional<Reservation> findReservationByDateTimeAndThemeId(LocalDate date, Long timeId,
        Long themeId) {
        String sql = """
                SELECT r.id, r.name, r.date, rt.id AS time_id, rt.start_at, t.id AS theme_id, t.name AS theme_name, t.description, t.image_url
                FROM reservation r
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                WHERE r.date = :date AND time_id = :timeId AND theme_id = :themeId
                """;
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
            "date", date,
            "timeId", timeId,
            "themeId", themeId
        ));
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, parameters, this::mapReservation);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> findTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = :date AND theme_id = :themeId";
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
            "date", date,
            "themeId", themeId
        ));

        return jdbcTemplate.query(
            sql,
            parameters,
            (resultSet, rowNum) -> resultSet.getLong("time_id"));
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> args = Map.of(
            "name", reservation.getName(),
            "date", reservation.getDate(),
            "time_id", reservation.getTime().getId(),
            "theme_id", reservation.getTheme().getId()
        );
        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(args).longValue();

        return Reservation.reconstruct(generatedKey, reservation.getName(), reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme());
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = :timeId)";
        SqlParameterSource parameters = new MapSqlParameterSource("timeId", timeId);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, parameters, Boolean.class));
    }

    @Override
    public int deleteReservationById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcTemplate.update(sql, parameters);
    }

    private Reservation mapReservation(ResultSet resultSet, int rowNum) throws SQLException {
        return Reservation.reconstruct(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            Time.reconstruct(
                resultSet.getLong("time_id"),
                LocalTime.parse(resultSet.getString("start_at"))
            ),
            Theme.reconstruct(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
            )
        );
    }
}

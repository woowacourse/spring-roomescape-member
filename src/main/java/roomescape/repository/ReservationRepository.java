package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationStatus;
import roomescape.domain.Theme;
import roomescape.global.exception.reservation.ReservationNotFoundException;

@Repository
public class ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        ReservationTime reservationTime = ReservationTime.from(
                rs.getLong("time_id"),
                rs.getObject("time_start_at", LocalTime.class)
        );
        Theme theme = Theme.from(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("image_path")
        );
        return Reservation.from(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                reservationTime,
                theme,
                ReservationStatus.valueOf(rs.getString("status"))
        );
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll(int size, int offset) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    r.status,
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.image_path
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                ORDER BY r.id
                LIMIT :size OFFSET :offset
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", offset);
        return jdbcTemplate.query(sql, parameters, reservationRowMapper);
    }

    public List<Reservation> findByName(String name) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    r.status,
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.image_path
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                WHERE r.name = :name
                ORDER BY r.id
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name);
        return jdbcTemplate.query(sql, parameters, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId())
                .addValue("status", reservation.getStatus().name());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Reservation.from(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme(), reservation.getStatus());
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    r.status,
                    rt.id AS time_id,
                    rt.start_at AS time_start_at,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.image_path
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                WHERE r.id = :id
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(sql, parameters, reservationRowMapper)
                .stream()
                .findFirst();
    }

    public Reservation updateSchedule(Reservation reservation) {
        String sql = """
                update reservation
                set date = :date, time_id = :timeId
                where id = :id
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", reservation.getId())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId());
        jdbcTemplate.update(sql, parameters);
        return reservation;
    }

    public Reservation updateStatus(Reservation reservation) {
        String sql = """
                update reservation
                set status = :status
                where id = :id
                """;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", reservation.getId())
                .addValue("status", reservation.getStatus().name());
        jdbcTemplate.update(sql, parameters);
        return reservation;
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = :id;";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        int deletedCount = jdbcTemplate.update(sql, parameters);

        if (deletedCount == 0) {
            throw new ReservationNotFoundException("해당 예약을 찾을 수 없습니다.");
        }
    }
}

package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
@RequiredArgsConstructor
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Theme theme = Theme.of(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("thumbnail_url"),
                rs.getString("theme_description")
        );

        ReservationTime reservationTime = ReservationTime.of(
                rs.getLong("time_id"),
                rs.getObject("time_value", LocalTime.class)
        );

        return Reservation.create(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                reservationTime,
                theme
        );
    };

    public Reservation save(Reservation reservation, long timeId, long themeId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.username())
                .addValue("date", reservation.reservationDate())
                .addValue("time_id", timeId)
                .addValue("theme_id", themeId);

        SimpleJdbcInsert reservationInsertExecutor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Number reservationId = reservationInsertExecutor.executeAndReturnKey(params);

        String sql = """
                SELECT 
                    reservation.id as reservation_id,
                    reservation.name,
                    reservation.date,
                    time.id as time_id,
                    time.start_at as time_value,
                    theme.id as theme_id,
                    theme.name as theme_name,
                    theme.thumbnail_url as thumbnail_url,
                    theme.description as theme_description 
                FROM reservation as reservation
                INNER JOIN reservation_time as time
                ON reservation.time_id = time.id
                INNER JOIN theme as theme
                ON reservation.theme_id = theme.id
                WHERE reservation.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, rowMapper, reservationId.longValue());
    }

    public void delete(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int affected = jdbcTemplate.update(sql, reservationId);

        if (affected == 0) {
            throw new NoSuchElementException("[ERROR] 삭제할 id에 해당하는 예약이 존재하지 않습니다.");
        }
    }

    public List<Reservation> findAllReservations() {
        String sql = """
                SELECT
                    reservation.id as reservation_id,
                    reservation.name,
                    reservation.date,
                    time.id as time_id,
                    time.start_at as time_value,
                    theme.id as theme_id,
                    theme.name as theme_name,
                    theme.thumbnail_url as thumbnail_url,
                    theme.description as theme_description 
                FROM reservation as reservation
                INNER JOIN reservation_time as time
                ON reservation.time_id = time.id
                INNER JOIN theme as theme
                ON reservation.theme_id = theme.id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
        SELECT EXISTS (
            SELECT 1 FROM reservation
            WHERE date = ? AND time_id = ? AND theme_id = ?
        )
        """;
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId)
        );
    }

}


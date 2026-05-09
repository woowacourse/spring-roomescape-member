package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    r.theme_id AS theme_id,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    t.end_at AS time_end_at,
                    h.name AS theme_name,
                    h.description AS theme_description,
                    h.thumbnail_url AS theme_thumbnail_url
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme h ON r.theme_id = h.id 
                ORDER BY r.id
                """;

        return jdbcTemplate.query(sql, this::mapToDomain)
                .stream()
                .toList();
    }

    public Reservation save(final Reservation newReservation) {
        final long newReservationId = insertReservation(newReservation);

        return newReservation.withId(newReservationId);
    }

    public boolean deleteById(final Long reservationId) {
        final String sql = """
                DELETE FROM reservation
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, reservationId) > 0;
    }

    public List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT
                    rt.id,
                    rt.start_at,
                    CASE
                        WHEN r.id IS NOT NULL THEN TRUE
                        ELSE FALSE
                    END AS reserved
                FROM reservation_time rt
                LEFT JOIN reservation r
                    ON r.time_id = rt.id
                   AND r.date = ?
                   AND r.theme_id = ?
                ORDER BY rt.start_at;
                """;

        return jdbcTemplate.query(
                        sql,
                        this::mapToTimesWithStatus,
                        date,
                        themeId
                ).stream()
                .toList();
    }


    private long insertReservation(final Reservation reservation) {
        final String sql = """
                INSERT INTO reservation (name, date, time_id, theme_id)
                VALUES (?, ?, ?, ?)
                """;

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());

            return preparedStatement;
        }, keyHolder);

        return generatedIdFrom(keyHolder);
    }

    private static long generatedIdFrom(final KeyHolder keyHolder) {
        final Number generatedKey = keyHolder.getKey();

        if (generatedKey == null) {
            throw new IllegalStateException("생성된 id를 가져오지 못했습니다.");
        }

        return generatedKey.longValue();
    }

    /**
     * ResultSet - Domain 매핑 메서드
     */
    private Reservation mapToDomain(final ResultSet resultSet, final int rowNum) throws SQLException {
        final ReservationTime reservationTime = ReservationTime.createWithId(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_start_at").toLocalTime(),
                resultSet.getTime("time_end_at").toLocalTime()
        );

        final Theme theme = Theme.createWithId(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail_url")
        );

        return Reservation.createWithId(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("reservation_date").toLocalDate(),
                reservationTime,
                theme
        );
    }

    /**
     * ResultSet - DTO 매핑 메서드
     */
    private ReservationTimesWithStatus mapToTimesWithStatus(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new ReservationTimesWithStatus(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime(),
                resultSet.getBoolean("reserved")
        );
    }
}

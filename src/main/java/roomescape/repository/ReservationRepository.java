package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.entity.ReservationEntity;

import java.sql.*;
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
        final ReservationEntity entity = toEntity(newReservation);

        final long newReservationId = insertReservation(entity);

        return newReservation.saved(newReservationId);
    }

    public boolean deleteById(final Long reservationId) {
        final String sql = """
                DELETE FROM reservation
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, reservationId) > 0;
    }


    private long insertReservation(final ReservationEntity reservationEntity) {
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

            preparedStatement.setString(1, reservationEntity.name());
            preparedStatement.setDate(2, reservationEntity.date());
            preparedStatement.setLong(3, reservationEntity.timeId());
            preparedStatement.setLong(4, reservationEntity.themeId());

            return preparedStatement;
        }, keyHolder);

        return generatedIdFrom(keyHolder);
    }

    private long generatedIdFrom(final KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("생성된 id를 가져오지 못했습니다.");
        }

        return keyHolder.getKey().longValue();
    }


    /**
     * 엔티티 - 도메인 매핑 메서드
     */
    private Reservation mapToDomain(
            final ResultSet resultSet,
            final int rowNum
    ) throws SQLException {
        final ReservationTime reservationTime = ReservationTime.restore(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_start_at").toLocalTime(),
                resultSet.getTime("time_end_at").toLocalTime()
        );

        final Theme theme = Theme.restore(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail_url")
        );

        return Reservation.restore(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("reservation_date").toLocalDate(),
                reservationTime,
                theme
        );
    }

    private ReservationEntity toEntity(final Reservation reservation) {
        return new ReservationEntity(
                reservation.getId(),
                reservation.getName(),
                Date.valueOf(reservation.getDate()),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );
    }
}

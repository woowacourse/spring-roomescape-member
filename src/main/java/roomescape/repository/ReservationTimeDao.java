package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.entity.ReservationTimeEntity;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public List<ReservationTime> findAll() {
        final String sql = """
                SELECT id, start_at
                FROM reservation_time
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, this::mapToEntity)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    public Optional<ReservationTime> findById(final Long timeId) {
        final String sql = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id = ?
                """;

        try {
            ReservationTimeEntity entity = jdbcTemplate.queryForObject(
                    sql,
                    this::mapToEntity,
                    timeId
            );

            return Optional.of(toDomain(entity));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public ReservationTime save(final ReservationTime newReservationTime) {
        ///
        final long newTimeId = insertReservationTime(newReservationTime);

        return newReservationTime.saved(newTimeId);
    }

    public boolean delete(final Long timeId) {
        final String sql = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, timeId) > 0;
    }


    private long insertReservationTime(final ReservationTime newReservationTime) {
        final String sql = """
                INSERT INTO reservation_time (start_at)
                VALUES (?)
                """;

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setTime(1, Time.valueOf(newReservationTime.getStartAt()));

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
    private ReservationTimeEntity mapToEntity(final ResultSet resultSet, final int rowNum) throws SQLException {
        return new ReservationTimeEntity(
                resultSet.getLong("id"),
                resultSet.getTime("start_at")
        );
    }

    private ReservationTimeEntity toEntity(final ReservationTime reservationTime) {
        return new ReservationTimeEntity(
                reservationTime.getId(),
                Time.valueOf(reservationTime.getStartAt())
        );
    }

    private ReservationTime toDomain(final ReservationTimeEntity entity) {
        return ReservationTime.restore(
                entity.id(),
                entity.startAt().toLocalTime()
        );
    }
}

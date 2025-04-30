package roomescape.time.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.jdbc.JdbcUtils;
import roomescape.time.application.converter.ReservationTimeConverter;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.infrastructure.entity.ReservationTimeEntity;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReservationTimeEntity> reservationTimeMapper = (resultSet, rowNum) -> ReservationTimeEntity.of(
            resultSet.getLong("id"),
            resultSet.getTime("start_at")
    );

    @Override
    public boolean existsById(final ReservationTimeId id) {
        final String sql = """
                select exists
                    (select 1 from reservation_time where id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, id.getValue()));
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        final String sql = """
                select exists
                    (select 1 from reservation_time where start_at = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
    }

    @Override
    public Optional<ReservationTime> findById(final ReservationTimeId id) {
        final String sql = "select id, start_at from reservation_time where id = ?";
        return JdbcUtils.queryForOptional(jdbcTemplate, sql, reservationTimeMapper, id.getValue())
                .map(ReservationTimeConverter::toDomain);
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "select id, start_at from reservation_time";

        return jdbcTemplate.query(sql, reservationTimeMapper).stream()
                .map(ReservationTimeConverter::toDomain)
                .toList();
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        final String sql = "insert into reservation_time (start_at) values (?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setTime(1, Time.valueOf(reservationTime.getValue()));

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return ReservationTime.of(ReservationTimeId.from(generatedId), reservationTime.getValue());
    }

    @Override
    public void deleteById(final ReservationTimeId id) {
        final String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id.getValue());
    }
}

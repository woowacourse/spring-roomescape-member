package roomescape.reservation.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.jdbc.JdbcUtils;
import roomescape.reservation.application.converter.ReservationConverter;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.infrastructure.entity.ReservationEntity;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.infrastructure.entity.ReservationTimeEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReservationEntity> reservationMapper = (resultSet, rowNum) -> {
        ReservationTimeEntity time = ReservationTimeEntity.of(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at")
        );

        return ReservationEntity.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date"),
                time
        );
    };

    @Override
    public boolean existsByParams(final ReservationId id) {
        final String sql = """
                select exists 
                    (select 1 from reservation where id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, id.getValue()));
    }

    @Override
    public boolean existsByParams(final ReservationTimeId timeId) {
        final String sql = """
                select exists 
                    (select 1 from reservation where time_id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, timeId.getValue()));
    }

    @Override
    public boolean existsByParams(final LocalDate date,
                                  final ReservationTimeId timeId,
                                  final ThemeId themeId) {
        final String sql = """
                select exists 
                    (select 1 from reservation where date = ? and time_id = ? and theme_id = ?)
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId.getValue(), themeId.getValue()));
    }

    @Override
    public Optional<Reservation> findById(final ReservationId id) {
        final String sql = """
                select
                    r.id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as start_at
                from reservation r
                join reservation_time t
                    on r.time_id = t.id
                where r.id = ?
                """;

        return JdbcUtils.queryForOptional(jdbcTemplate, sql, reservationMapper, id.getValue())
                .map(ReservationConverter::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select
                    r.id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as started_at
                from reservation r
                join reservation_time t
                    on r.time_id = t.id
                """;

        return jdbcTemplate.query(sql, reservationMapper).stream()
                .map(ReservationConverter::toDomain)
                .toList();
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, reservation.getName().getValue());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate().getValue()));
            preparedStatement.setLong(3, reservation.getTime().getId().getValue());

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return Reservation.withId(
                ReservationId.from(generatedId),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime());
    }

    @Override
    public void deleteById(final ReservationId id) {
        final String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id.getValue());
    }
}

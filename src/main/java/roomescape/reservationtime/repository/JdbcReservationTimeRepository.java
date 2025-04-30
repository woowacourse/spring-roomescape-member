package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcReservationTimeRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("reservation_time")
                .usingGeneratedKeyColumns("id")
                .usingColumns("start_at");
    }

    @Override
    public ReservationTime save(final LocalTime startAt) {
        final String time = startAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", time);
        final Long newId = inserter.executeAndReturnKey(params).longValue();
        return new ReservationTime(newId, startAt);
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "select id, start_at from reservation_time";
        return template.query(sql, actorRowMapper);
    }

    @Override
    public List<AvailableReservationTimeResponse> findAllAvailable(LocalDate date, Long themeId) {
        String sql = """
                select
                    rt.id,
                    rt.start_at,
                    (r.id is not null) as alreadyBooked
                from reservation_time rt
                left join reservation r
                    on rt.id = r.time_id
                    and r.date = ?
                    and r.theme_id = ?
                order by rt.start_at
                """;

        return template.query(sql, (rs, rowNum) ->
                        new AvailableReservationTimeResponse(
                                rs.getLong("id"),
                                rs.getTime("start_at").toLocalTime(),
                                rs.getBoolean("alreadyBooked")
                        ),
                date, themeId
        );
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from reservation_time where id = ?";
        final int rowsAffected = template.update(sql, id);

        if (rowsAffected != 1) {
            throw new IllegalArgumentException("삭제할 예약 시간이 없습니다. id=" + id);
        }
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String sql = "select id, start_at from reservation_time where id = ?";
        return template.query(sql, actorRowMapper, id)
                .stream()
                .findFirst();
    }

    private final RowMapper<ReservationTime> actorRowMapper = (resultSet, rowNum) -> {
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        final LocalTime time = LocalTime.parse(resultSet.getString("start_at"), timeFormatter);
        return new ReservationTime(resultSet.getLong("id"), time);
    };
}

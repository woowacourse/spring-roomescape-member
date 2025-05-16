package roomescape.infra;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.error.NotFoundException;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> {
        final LocalTime time = LocalTime.parse(resultSet.getString("start_at"));
        return new ReservationTime(resultSet.getLong("id"), time);
    };

    private static final RowMapper<AvailableReservationTimeResponse> availableTimeRowMapper = (resultSet, rowNum) ->
            new AvailableReservationTimeResponse(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime(),
                    resultSet.getBoolean("alreadyBooked")
            );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcReservationTimeRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("reservation_time")
                .usingGeneratedKeyColumns("id")
                .usingColumns("start_at");
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        final Long newId = inserter.executeAndReturnKey(params).longValue();
        return new ReservationTime(newId, reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "select id, start_at from reservation_time";
        return template.query(sql, timeRowMapper);
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

        return template.query(sql, availableTimeRowMapper, date, themeId);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from reservation_time where id = ?";
        final int rowsAffected = template.update(sql, id);

        if (rowsAffected != 1) {
            throw new NotFoundException("삭제할 예약 시간이 없습니다. id=" + id);
        }
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String sql = "select id, start_at from reservation_time where id = ?";
        return template.query(sql, timeRowMapper, id)
                .stream()
                .findFirst();
    }
}

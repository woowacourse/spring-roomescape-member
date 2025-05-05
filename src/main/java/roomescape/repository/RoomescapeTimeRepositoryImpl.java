package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.exception.DeletionNotAllowedException;

@Repository
public class RoomescapeTimeRepositoryImpl implements RoomescapeTimeRepository {

    private static final int SUCCESS_COUNT = 1;

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public RoomescapeTimeRepositoryImpl(final DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        String sql = "select * from reservation_time where id=:id";
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            ReservationTime reservationTime = template.queryForObject(sql, param, reservationTimeRowMapper());
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";
        return template.query(sql, reservationTimeRowMapper());
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        SqlParameterSource param = new MapSqlParameterSource("start_at", reservationTime.getStartAt());
        Number key = insert.executeAndReturnKey(param);
        return reservationTime.toEntity(key.longValue());
    }

    @Override
    public boolean deleteById(final Long id) {
        try {
            String sql = "delete from reservation_time where id = :id";
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            return template.update(sql, param) == SUCCESS_COUNT;
        } catch (DataIntegrityViolationException e) {
            throw new DeletionNotAllowedException("예약 시간을 지울 수 없습니다.");
        }
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (rs, rowNum) -> {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getString("start_at")
            );
        };
    }
}

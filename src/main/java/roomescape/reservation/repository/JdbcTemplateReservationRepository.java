package roomescape.reservation.repository;

import static roomescape.time.repository.ReservationTimeRowMapper.RESERVATION_TIME_ROW_MAPPER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNumber) -> Reservation.of(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            RESERVATION_TIME_ROW_MAPPER.mapRow(resultSet, rowNumber)
    );

    public JdbcTemplateReservationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date,
                       rt.id, rt.start_at
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date,
                       rt.id, rt.start_at
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                WHERE r.id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, reservationRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.time().id());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        int deletedCount = jdbcTemplate.update(sql, params);
        if (deletedCount == 0) {
            throw new NotFoundException("예약을 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE DATE = :date AND time_id = :time_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("time_id", timeId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}

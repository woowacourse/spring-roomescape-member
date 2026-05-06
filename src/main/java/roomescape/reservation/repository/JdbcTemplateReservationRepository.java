package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNumber) -> Reservation.of(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            resultSet.getTime("start_at").toLocalTime(),
            Theme.load(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url"),
                    resultSet.getBoolean("is_active")
            ),
            ReservationStatus.valueOf(resultSet.getString("status"))
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
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.start_at, 
                    r.date,
                    r.status,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.thumbnail_url,
                    t.is_active
                FROM reservation r
                INNER JOIN theme t ON r.theme_id = t.id
                """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), reservationRowMapper);
    }



    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.start_at, 
                    r.date,
                    r.status,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.thumbnail_url,
                    t.is_active
                FROM reservation r
                INNER JOIN theme t ON r.theme_id = t.id
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
                .addValue("start_at", reservation.time())
                .addValue("theme_id", reservation.theme().id())
                .addValue("status", reservation.status().name());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    // TODO boolean으로 변환후 예외처리는 비즈니스로직에서 수행
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
    public boolean existsByDateAndTimeAndThemeId(LocalDate date, LocalTime time, Long themeId){
        String sql = "SELECT COUNT(*) FROM reservation WHERE DATE = :date AND start_at = :start_at AND theme_id = :theme_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("start_at", time)
                .addValue("theme_id", themeId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByNameAndDateAndTime(String name, LocalDate date, LocalTime time) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE name = :name AND date = :date AND start_at = :start_at";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("start_at", time);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    public boolean updateStatus(Reservation reservation) {
        String sql = """
                UPDATE RESERVATION
                SET status = :status
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", reservation.id())
                .addValue("status", reservation.status().name());
        int updatedCount = jdbcTemplate.update(sql, params);
        return updatedCount > 0;
    }

}

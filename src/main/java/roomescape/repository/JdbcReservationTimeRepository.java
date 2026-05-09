package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final String FIND_RESERVED_TIMES_BY_DATE_AND_THEME = """
            SELECT id, start_at
            FROM reservation_time
            WHERE id IN (
              SELECT rt.id
              FROM reservation_time AS rt
              INNER JOIN reservation AS re
              ON rt.id = re.time_id
              WHERE re.date = ?
              AND re.theme_id = ?
            )
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(reservationTime)
        ).longValue();

        return new ReservationTime(
                generatedKey,
                reservationTime.getStartAt()
        );
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";

        return jdbcTemplate.query(
                sql,
                getReservationTimeRowMapper()
        );
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";

        List<ReservationTime> results = jdbcTemplate.query(
                sql,
                getReservationTimeRowMapper(),
                id
        );
        return results.stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> findReservedTimes(LocalDate selectedDate, Long themeId) {
        return jdbcTemplate.query(
                FIND_RESERVED_TIMES_BY_DATE_AND_THEME,
                getReservationTimeRowMapper(),
                selectedDate, themeId
        );
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return new DataClassRowMapper<>(ReservationTime.class);
    }
}

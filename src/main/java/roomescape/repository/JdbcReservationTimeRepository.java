package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<ReservationTime> rowMapper;

    public JdbcReservationTimeRepository(DataSource dataSource, RowMapper<ReservationTime> rowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public ReservationTime insertReservationTime(ReservationTime reservationTime) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        long savedId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findReservationTimeById(savedId);
    }

    @Override
    public void deleteReservationTimeById(long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public boolean isExistTimeOf(String startAt) {
        String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = :startAt";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("startAt", startAt);
        int count = jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
        return count > 0;
    }

    @Override
    public boolean isExistTimeOf(long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation_time WHERE id = :timeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("timeId", timeId);
        int count = jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
        return count > 0;
    }

    @Override
    public ReservationTime findReservationTimeById(long savedId) {
        String sql = """
                SELECT 
                t.id, 
                start_at 
                FROM reservation_time AS t 
                WHERE t.id = :savedId;
                """;
        SqlParameterSource paramMap = new MapSqlParameterSource().addValue("savedId", savedId);
        return jdbcTemplate.query(sql, paramMap, rowMapper).get(0);
    }

    @Override
    public List<ReservationTime> findBookedTimeForThemeAtDate(String date, long themeId) {
        String sql = """
                SELECT 
                t.id AS time_id, 
                t.start_at AS time_value 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id 
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date = :date
                AND r.theme_id = :themeId
                """;

        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return jdbcTemplate.query(sql, paramMap, rowMapper);
    }
}

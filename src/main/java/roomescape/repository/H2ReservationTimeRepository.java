package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ReservationTimeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("ID");
    }

    private ReservationTime mapRowTime(ResultSet rs, int rowNum) throws SQLException {
        return new ReservationTime(
                rs.getLong("ID"),
                LocalTime.parse(rs.getString("START_AT"))
        );
    }

    @Override
    public List<ReservationTime> findAllByOrderByStartAt() {
        String sql = """
                SELECT * FROM RESERVATION_TIME
                ORDER BY START_AT
                """;

        return jdbcTemplate.query(sql, this::mapRowTime);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        String sql = "SELECT * FROM RESERVATION_TIME WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapRowTime, id)
                .stream()
                .findAny();
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        String sql = "SELECT * FROM RESERVATION_TIME WHERE START_AT = ? LIMIT 1";
        String formattedStartAt = startAt.format(DateTimeFormatter.ofPattern("HH:mm"));

        return !jdbcTemplate.query(sql, this::mapRowTime, formattedStartAt)
                .isEmpty();
    }


    @Override
    public ReservationTime save(ReservationTime time) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(time);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return time.assignId(id);
    }

    @Override
    public int delete(long id) {
        String sql = "DELETE FROM RESERVATION_TIME WHERE ID = ?";

        return jdbcTemplate.update(sql, id);
    }
}

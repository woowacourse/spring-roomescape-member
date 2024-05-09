package roomescape.reservation.dao;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Reservation> rowMapper = (ResultSet resultSet, int rowNum) -> {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail"))
        );
    };

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id as reservation_id, r.date, t.id as time_id, t.start_at as time_value, th.id as theme_id, th.name as theme_name, th.description, th.thumbnail 
                FROM reservation as r 
                INNER JOIN reservation_time as t on r.time_id = t.id 
                INNER JOIN theme as th on r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int updateId = jdbcTemplate.update(sql, reservationId);
        return updateId != 0;
    }

    @Override
    public boolean existByTimeId(long timeId) {
        String sql = """
                SELECT COUNT(*) FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                WHERE t.id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existBy(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT COUNT(*) 
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                INNER JOIN theme as th on r.theme_id = th.id
                WHERE r.date = ? AND t.id = ? AND r.theme_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }

    @Override
    public void saveReservationList(long memberId, long reservationId) {
        String sql = "INSERT INTO reservation_list(member_id, reservation_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, memberId, reservationId);
    }

    @Override
    public long findMemberIdByReservationId(long reservationId) {
        String sql = "SELECT member_id FROM reservation_list WHERE reservation_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reservationId);
    }
}

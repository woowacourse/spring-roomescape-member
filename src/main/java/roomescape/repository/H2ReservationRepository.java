package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class H2ReservationRepository implements ReservationRepository {
    private final ReservationRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ReservationRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(getBasicSelectQuery(), rowMapper);
    }

    public Reservation save(Reservation reservation) {
        long reservationId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", reservation.getName().value(),
                        "date", reservation.getDate(),
                        "time_id", reservation.getTime().getId(),
                        "theme_id", reservation.getTheme().getId()))
                .longValue();

        return new Reservation(
                reservationId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme());
    }

    public void deleteById(long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, long themeId) {
        String conditionQuery = " where r.date = ? and tm.id = ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    public List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate) {
        String conditionQuery = " where r.date between ? and ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    private String getBasicSelectQuery() {
        return """
                    select 
                        r.id as reservation_id,
                        r.name as reservation_name,
                        r.date as reservation_date,
                        t.id as time_id,
                        t.start_at as time_value,
                        tm.id as theme_id,
                        tm.name as theme_name,
                        tm.description as theme_description,
                        tm.thumbnail as theme_thumbnail
                    from reservation as r
                    inner join reservation_time as t
                    on r.time_id = t.id
                    inner join theme as tm
                    on r.theme_id = tm.id 
                """;
    }

    static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(
                    rs.getLong("reservation_id"),
                    new Name(rs.getString("name")),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("time_value").toLocalTime()),
                    new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("theme_description"),
                            rs.getString("theme_thumbnail")
                    ));
        }
    }
}

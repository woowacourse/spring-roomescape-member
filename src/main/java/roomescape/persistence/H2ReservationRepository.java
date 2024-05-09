package roomescape.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Name;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;

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

    public Reservation save(Reservation reservation) {
        try {
            long reservationId = jdbcInsert.executeAndReturnKey(Map.of(
                            "name", reservation.getName().value(),
                            "date", reservation.getDate().getStartAt(),
                            "time_id", reservation.getTime().getId(),
                            "theme_id", reservation.getTheme().getId()))
                    .longValue();

            return new Reservation(
                    reservationId,
                    reservation.getName(),
                    reservation.getDate(),
                    reservation.getTime(),
                    reservation.getTheme());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("예약 정보가 올바르지 않습니다.");
        }
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(getBasicSelectQuery(), rowMapper);
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String conditionQuery = " where r.date = ? and tm.id = ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    public List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate) {
        String conditionQuery = " where r.date between ? and ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByThemeAndDateTime(Theme theme, ReservationDate date, ReservationTime time) {
        String sql = """
                    select exists(
                    select 1
                    from reservation as r
                    where r.theme_id = ?
                    and r.date = ?
                    and r.time_id = ?) as count;
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, theme.getId(), date.getStartAt(), time.getId());
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

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(
                    rs.getLong("reservation_id"),
                    new Name(rs.getString("name")),
                    new ReservationDate(rs.getDate("date").toLocalDate()),
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

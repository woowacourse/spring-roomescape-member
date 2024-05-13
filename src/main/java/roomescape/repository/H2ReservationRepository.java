package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.infrastructure.QueryBuilder;

@Repository
public class H2ReservationRepository implements ReservationRepository {
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String MEMBER_ID = "member_id";
    private static final String TIME_ID = "time_id";
    private static final String THEME_ID = "theme_id";

    private final ReservationRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ReservationRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ReservationRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns(ID);
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(getBasicSelectQuery(), rowMapper);
    }

    public Reservation save(Reservation reservation) {
        Long reservationId = jdbcInsert.executeAndReturnKey(Map.of(
                        DATE, reservation.getDate(),
                        MEMBER_ID, reservation.getMember().getId(),
                        TIME_ID, reservation.getTimeId(),
                        THEME_ID, reservation.getThemeId()
        )).longValue();

        return new Reservation(
                reservationId,
                reservation.getDate(),
                reservation.getMember(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String conditionQuery = " where r.date = ? and tm.id = ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    public List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate) {
        String conditionQuery = " where r.date >= ? and r.date <= ?";
        String sql = getBasicSelectQuery() + conditionQuery;

        return jdbcTemplate.query(sql, rowMapper, startDate, endDate);
    }

    @Override
    public List<Reservation> searchReservations(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        QueryBuilder queryBuilder = QueryBuilder.build(getBasicSelectQuery())
                .addCondition(themeId, "tm.id = ?")
                .addCondition(memberId, "u.id = ?")
                .addCondition(dateFrom, "r.date >= ?")
                .addCondition(dateTo, "r.date <= ?");
        return jdbcTemplate.query(queryBuilder.getQuery(), rowMapper, queryBuilder.getParam());
    }

    public boolean existsByDateTimeAndTheme(Reservation reservation) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql, Boolean.class, reservation.getDate(), reservation.getTimeId(), reservation.getThemeId()
        ));
    }

    private String getBasicSelectQuery() {
        return """
                    select 
                        r.id as reservation_id,
                        r.date as reservation_date,
                        t.id as time_id,
                        t.start_at as time_value,
                        tm.id as theme_id,
                        tm.name as theme_name,
                        tm.description as theme_description,
                        tm.thumbnail as theme_thumbnail,
                        u.id as user_id,
                        u.name as user_name,
                        u.email as user_email,
                        u.password as user_password,
                        u.role as user_role
                    from reservation as r
                    inner join reservation_time as t
                    on r.time_id = t.id
                    inner join theme as tm
                    on r.theme_id = tm.id 
                    inner join user_table as u
                    on r.member_id = u.id
                """;
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getDate(DATE).toLocalDate(),
                    new Member(rs.getLong("user_id"),
                            rs.getString("user_name"),
                            rs.getString("email"),
                            rs.getString("user_password"),
                            Role.valueOf(rs.getString("user_role"))
                    ),
                    new ReservationTime(
                            rs.getLong(TIME_ID),
                            rs.getTime("time_value").toLocalTime()
                    ),
                    new Theme(
                            rs.getLong(THEME_ID),
                            rs.getString("theme_name"),
                            rs.getString("theme_description"),
                            rs.getString("theme_thumbnail")
                    )
            );
        }
    }
}

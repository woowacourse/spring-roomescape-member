package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.controller.api.reservation.dto.ReservationSearchFilter;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String BASE_SELECT_SQL = """
            SELECT 
              R.ID AS R_ID,
              R.MEMBER_ID AS R_MEMBER_ID,
              R.DATE AS R_DATE,
              R.TIME_ID AS R_TIME_ID,
              R.THEME_ID AS R_THEME_ID,
              RT.ID AS RT_ID,
              RT.START_AT AS RT_START_AT,
              T.ID AS T_ID,
              T.NAME AS T_NAME,
              T.DESCRIPTION AS T_DESCRIPTION,
              T.THUMBNAIL AS T_THUMBNAIL,
              M.ID AS M_ID,
              M.EMAIL AS M_EMAIL,
              M.PASSWORD AS M_PASSWORD,
              M.NAME AS M_NAME,
              M.ROLE AS M_ROLE
            FROM RESERVATION R
            JOIN RESERVATION_TIME RT ON R.TIME_ID = RT.ID
            JOIN THEME T ON R.THEME_ID = T.ID
            JOIN MEMBER M ON R.MEMBER_ID = M.ID 
            """;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(BASE_SELECT_SQL, createReservationRowMapper());
    }

    @Override
    public List<Reservation> findAllByFilter(final ReservationSearchFilter filter) {
        final SqlCondition sqlCondition = createConditionClauseOfFilter(filter);
        return findAllByCondition(sqlCondition);
    }

    @Override
    public List<Reservation> findAllByTimeSlotId(final Long id) {
        final SqlCondition sqlCondition = createConditionClause(List.of("RT.ID = ?"), id);
        return findAllByCondition(sqlCondition);
    }

    @Override
    public List<Reservation> findAllByThemeId(final Long id) {
        final SqlCondition sqlCondition = createConditionClause(List.of("T.ID = ?"), id);
        return findAllByCondition(sqlCondition);
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        final SqlCondition sqlCondition = createConditionClause(List.of("R.DATE = ?", "T.ID = ?"), date, themeId);
        return findAllByCondition(sqlCondition);
    }

    private List<Reservation> findAllByCondition(final SqlCondition sqlCondition) {
        final StringBuilder sql = new StringBuilder(BASE_SELECT_SQL);
        if (sqlCondition != null && sqlCondition.clause() != null && !sqlCondition.clause().isBlank()) {
            sql.append(" WHERE ").append(sqlCondition.clause());
        }
        return jdbcTemplate.query(sql.toString(), createReservationRowMapper(), sqlCondition.params());
    }

    private SqlCondition createConditionClause(final List<String> conditions, final Object... params) {
        if (conditions.isEmpty()) {
            return new SqlCondition("", new Object[]{});
        }
        final String clause = String.join(" AND ", conditions);
        return new SqlCondition(clause, params);
    }

    private SqlCondition createConditionClauseOfFilter(final ReservationSearchFilter filter) {
        final List<String> conditions = new ArrayList<>();
        final List<Object> params = new ArrayList<>();

        addCondition(conditions, params, "R.MEMBER_ID = ?", filter.memberId());
        addCondition(conditions, params, "R.THEME_ID = ?", filter.themeId());
        addCondition(conditions, params, "R.DATE >= ?", filter.dateFrom());
        addCondition(conditions, params, "R.DATE <= ?", filter.dateTo());

        final String clause = conditions.isEmpty() ? "" : String.join(" AND ", conditions);
        System.out.println(clause);
        return new SqlCondition(clause, params.toArray());
    }

    private <T> void addCondition(List<String> conditions, List<Object> params, String sql, T value) {
        if (value != null) {
            conditions.add(sql);
            params.add(value);
        }
    }

    @Override
    public List<Theme> findPopularThemesByPeriod(LocalDate startDate, LocalDate endDate, Integer limit) {
        final String sql = """
                SELECT R.THEME_ID, T.NAME AS T_NAME, T.THUMBNAIL AS T_THUMBNAIL, 
                       T.DESCRIPTION AS T_DESCRIPTION, COUNT(R.ID) AS CNT
                FROM RESERVATION R
                JOIN THEME T ON T.ID = R.THEME_ID
                WHERE R.DATE BETWEEN ? AND ?
                GROUP BY R.THEME_ID
                ORDER BY CNT DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Theme(
                rs.getLong("THEME_ID"),
                rs.getString("T_NAME"),
                rs.getString("T_DESCRIPTION"),
                rs.getString("T_THUMBNAIL")
        ), startDate, endDate, limit);
    }

    @Override
    public Long save(Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("MEMBER_ID", reservation.member().id())
                .addValue("DATE", reservation.date())
                .addValue("TIME_ID", reservation.timeSlot().id())
                .addValue("THEME_ID", reservation.theme().id());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Boolean removeById(Long id) {
        final String sql = "DELETE FROM RESERVATION WHERE ID = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private RowMapper<Reservation> createReservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("R_ID"),
                new Member(
                        rs.getLong("M_ID"),
                        rs.getString("M_NAME"),
                        rs.getString("M_EMAIL"),
                        rs.getString("M_PASSWORD"),
                        Role.findByName(rs.getString("M_ROLE"))
                ),
                rs.getDate("R_DATE").toLocalDate(),
                new TimeSlot(
                        rs.getLong("RT_ID"),
                        rs.getTime("RT_START_AT").toLocalTime()
                ),
                new Theme(
                        rs.getLong("T_ID"),
                        rs.getString("T_NAME"),
                        rs.getString("T_DESCRIPTION"),
                        rs.getString("T_THUMBNAIL")
                )
        );
    }

    private record SqlCondition(String clause, Object[] params) {
    }
}

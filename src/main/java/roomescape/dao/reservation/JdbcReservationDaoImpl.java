package roomescape.dao.reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.reservation.InvalidReservationException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
public class JdbcReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAllReservation() {
        final String query = """
                   select r.id as reservation_id, 
                
                          m.id as member_id, 
                          m.name as member_name,
                          m.email as member_email,
                          m.password as member_password,
                          m.role as member_role,
                
                          r.date,
                
                          rt.id as time_id,
                          rt.start_at as time_value,
                
                          t.id as theme_id,
                          t.name as theme_name,
                          t.description as theme_description,
                          t.thumbnail as theme_thumbnail
                   from reservation as r 
                   inner join member as m on r.member_id = m.id
                   inner join reservation_time as rt on r.time_id = rt.id
                   inner join theme as t on r.theme_id = t.id 
                """;

        return jdbcTemplate.query(query, getReservationRowMapper());
    }

    @Override
    public List<Reservation> findByThemeIdAndMemberIDAndDateFromAndDateTo(
            final LocalDate dateFrom,
            final LocalDate dateTo,
            final Long themeId,
            final Long memberId
    ) {
        final StringBuilder query = new StringBuilder("""
                SELECT
                                r.id AS reservation_id,
                                m.id AS member_id,
                                m.name AS member_name,
                                m.email AS member_email,
                                m.password AS member_password,
                                m.role AS member_role,
                                r.date,
                                t.id AS time_id,
                                t.start_at AS time_value,
                                th.id AS theme_id,
                                th.name AS theme_name,
                                th.description AS theme_description,
                                th.thumbnail AS theme_thumbnail
                            FROM reservation AS r
                            JOIN reservation_time AS t ON r.time_id = t.id
                            JOIN theme AS th ON r.theme_id = th.id
                            JOIN member AS m ON r.member_id = m.id
                            WHERE 1=1
                """);
        final List<Object> params = getDynamicQueryCondition(themeId, memberId, dateFrom, dateTo, query);

        return jdbcTemplate.query(query.toString(), params.toArray(), getReservationRowMapper());
    }

    private List<Object> getDynamicQueryCondition(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                                  final LocalDate dateTo, final StringBuilder query) {
        final List<Object> params = new ArrayList<>();
        if (themeId != null) {
            query.append(" AND th.id = ?");
            params.add(themeId);
        }
        if (memberId != null) {
            query.append(" AND m.id = ?");
            params.add(memberId);
        }
        if (dateFrom != null) {
            query.append(" AND r.date >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null) {
            query.append(" AND r.date <= ?");
            params.add(dateTo);
        }
        return params;
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, RowNum) ->
                new Reservation(
                        resultSet.getLong("reservation_id"),
                        createMember(resultSet),
                        createReservationDate(resultSet),
                        createReservationTime(resultSet),
                        createTheme(resultSet)
                );
    }

    private Member createMember(final ResultSet resultSet) throws SQLException {
        return Member.from(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                resultSet.getString("member_password"),
                MemberRole.from(resultSet.getString("member_role"))
        );
    }

    private ReservationDate createReservationDate(final ResultSet resultSet) throws SQLException {
        return new ReservationDate(LocalDate.parse(resultSet.getString("date")));
    }

    private ReservationTime createReservationTime(final ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("time_id"),
                LocalTime.parse(resultSet.getString("time_value")));
    }

    private Theme createTheme(final ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );
    }

    @Override
    public void saveReservation(final Reservation reservation) {
        final Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("member_id", reservation.getMemberId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        try {
            final Number newId = insertActor.executeAndReturnKey(parameters);
            reservation.setId(newId.longValue());
        } catch (final DataIntegrityViolationException e) {
            throw new InvalidReservationException("존재하지 않는 회원, 테마, 시간이 존재합니다.");
        }
    }

    @Override
    public void deleteReservation(final Long id) {
        final String query = "delete from reservation where id = ?";
        final int deletedRowCount = jdbcTemplate.update(query, id);
        validateDeleteRowCount(deletedRowCount);
    }

    private void validateDeleteRowCount(final int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new InvalidReservationException("삭제하려는 ID의 예약이 존재하지 않습니다.");
        }
    }

    @Override
    public Boolean existsReservationBy(final LocalDate date, final Long timeId, final Long themeId) {
        final String query = "select exists (select 1 from reservation where date = ? AND time_id = ? AND theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId);
    }
}

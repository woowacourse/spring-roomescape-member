package roomescape.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;

@Repository
public class JdbcReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAllReservation() {
        String query = """
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
    public List<Reservation> findByDate(final LocalDate dateFrom, final LocalDate dateTo) {
        String query = """
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
                
                   where date >= ? and date <= ?
                """;

        return jdbcTemplate.query(query, getReservationRowMapper(), dateFrom, dateTo);
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

    private Member createMember(ResultSet resultSet) throws SQLException {
        return Member.from(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                resultSet.getString("member_password"),
                MemberRole.from(resultSet.getString("member_role"))
        );
    }

    private ReservationDate createReservationDate(ResultSet resultSet) throws SQLException {
        return new ReservationDate(LocalDate.parse(resultSet.getString("date")));
    }

    private ReservationTime createReservationTime(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("time_id"),
                LocalTime.parse(resultSet.getString("time_value")));
    }

    private Theme createTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );
    }

    @Override
    public void saveReservation(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("member_id", reservation.getMemberId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        try {
            Number newId = insertActor.executeAndReturnKey(parameters);
            reservation.setId(newId.longValue());
        } catch (DataIntegrityViolationException e) {
            throw new InvalidReservationException("존재하지 않는 회원, 테마, 시간이 존재합니다.");
        }
    }

    @Override
    public void deleteReservation(Long id) {
        String query = "delete from reservation where id = ?";
        int deletedRowCount = jdbcTemplate.update(query, id);
        validateDeleteRowCount(deletedRowCount);
    }

    private void validateDeleteRowCount(final int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new InvalidReservationException("삭제하려는 ID의 예약이 존재하지 않습니다.");
        }
    }

    @Override
    public Boolean existsReservationBy(LocalDate date, Long timeId, Long themeId) {
        String query = "select exists (select 1 from reservation where date = ? AND time_id = ? AND theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId);
    }
}

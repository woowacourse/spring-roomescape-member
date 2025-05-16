package roomescape.domain.reservation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.model.Role;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.domain.theme.model.Theme;

@Repository
public class JdbcReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
            select
                        m.name as member_name,
                        m.id as member_id,
                        m.role as member_role,
                        m.email as member_email,
                        m.password as member_password,
                        r.id as reservation_id,
                        r.date as reservation_date,
                        rt.id as time_id,
                        rt.start_at,
                        t.id as theme_id,
                        t.name as theme_name,
                        t.description as theme_description,
                        t.thumbnail as theme_thumbnail
            from reservation r
            inner join member m on r.member_id = m.id
            inner join reservation_time rt on r.time_id = rt.id
            inner join theme t on r.theme_id = t.id  
            """;

        return jdbcTemplate.query(sql,
            (resultSet, RowNum) ->
                new Reservation(resultSet.getLong("reservation_id"),
                    createMember(resultSet),
                    createReservationDate(resultSet),
                    createReservationTime(resultSet),
                    createTheme(resultSet)
                ));
    }

    private ReservationDate createReservationDate(ResultSet resultSet) throws SQLException {
        return new ReservationDate(LocalDate.parse(resultSet.getString("reservation_date")));
    }

    private Member createMember(ResultSet resultSet) throws SQLException {
        return Member.createMember(
            resultSet.getLong("member_id"),
            Role.convertFrom(resultSet.getString("member_role")),
            resultSet.getString("member_name"),
            resultSet.getString("member_email"),
            resultSet.getString("member_password")
        );
    }

    private Theme createTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
            resultSet.getLong("theme_id"),
            resultSet.getString("theme_name"),
            resultSet.getString("theme_description"),
            resultSet.getString("theme_thumbnail")
        );
    }

    private ReservationTime createReservationTime(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
            resultSet.getLong("time_id"),
            LocalTime.parse(resultSet.getString("start_at")));
    }

    @Override
    public long save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("member_id", reservation.getMemberId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        parameters.put("theme_id", reservation.getThemeId());

        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean delete(Long id) {
        String query = "delete from reservation where id = ?";
        int update = jdbcTemplate.update(query, id);
        return update > 0;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String query = """
                select
                        m.name as member_name,
                        m.role as member_role,
                        m.id as member_id,
                        m.email as member_email,
                        m.password as member_password,
                        r.id as reservation_id,
                        r.date as reservation_date,
                        rt.id as time_id,
                        rt.start_at,
                        t.id as theme_id,
                        t.name as theme_name,
                        t.description as theme_description,
                        t.thumbnail as theme_thumbnail
            from reservation r
            inner join member m on r.member_id = m.id
            inner join reservation_time rt on r.time_id = rt.id
            inner join theme t on r.theme_id = t.id
            where r.id = ?
            """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) ->
                    new Reservation(
                        resultSet.getLong("reservation_id"),
                        createMember(resultSet),
                        createReservationDate(resultSet),
                        createReservationTime(resultSet),
                        createTheme(resultSet)
                    ),
                id
            );
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existReservationByTime(Long id) {
        String query = "select exists (select 1 from reservation where time_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, id);
    }

    @Override
    public boolean existReservationByTheme(Long id) {
        String query = "select exists (select 1 from reservation where theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, id);
    }

    @Override
    public boolean existReservationOf(ReservationDate date, Long themeId, Long timeId) {
        String query = "select exists (select 1 from reservation where date = ? AND theme_id = ? AND time_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, date.getDate(), themeId, timeId);
    }

    @Override
    public List<Reservation> findOf(String dateFrom, String dateTo, Long memberId, Long themeId) {
        String query = """
            select
                        m.name as member_name,
                        m.id as member_id,
                        m.role as member_role,
                        m.email as member_email,
                        m.password as member_password,
                        r.id as reservation_id,
                        r.date as reservation_date,
                        rt.id as time_id,
                        rt.start_at,
                        t.id as theme_id,
                        t.name as theme_name,
                        t.description as theme_description,
                        t.thumbnail as theme_thumbnail
            from reservation r
            inner join member m on r.member_id = m.id
            inner join reservation_time rt on r.time_id = rt.id
            inner join theme t on r.theme_id = t.id          
            where member_id = ?
            AND theme_id = ? 
            AND r.date between ? AND ?
            """;
        return jdbcTemplate.query(query,
            (resultSet, rowNum) -> {
                return new Reservation(
                    resultSet.getLong("id"),
                    createMember(resultSet),
                    createReservationDate(resultSet),
                    createReservationTime(resultSet),
                    createTheme(resultSet)
                );
            }, memberId, themeId, dateFrom, dateTo);
    }
}

package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> mapper;

    static {
        mapper = (resultSet, resultNumber) -> new Reservation(
                resultSet.getLong("reservation_id"),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ),

                resultSet.getDate("date").toLocalDate(),

                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),

                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }

    private final JdbcTemplate template;
    private SimpleJdbcInsert insertReservation;

    public H2ReservationRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT "
                + "r.id as reservation_id, r.date,"
                + "m.id as member_id, m.name as member_name, m.email, m.password, m.role,"
                + "rt.id as time_id, rt.start_at, "
                + "t.id as theme_id, t.name as theme_name, t.description, t.thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN member AS m ON r.member_id = m.id "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id ";
        return template.query(sql, mapper);
    }

    @Override
    public List<Reservation> saerch(
            final Long themeId,
            final Long memberId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    ) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.date,"
                + "m.id as member_id, m.name as member_name, m.email, m.password, m.role,"
                + "rt.id as time_id, rt.start_at, "
                + "t.id as theme_id, t.name as theme_name, t.description, t.thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN member AS m ON r.member_id = m.id "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id "
                + "WHERE t.id = ? "
                + "AND m.id = ? "
                + "AND r.date >= ? AND r.date <= ?";

        return template.query(sql, mapper, themeId, memberId, dateFrom, dateTo);
    }

    public Optional<Reservation> findById(long id) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.date,"
                + "m.id as member_id, m.name as member_name, m.email, m.password, m.role,"
                + "rt.id as time_id, rt.start_at, "
                + "t.id as theme_id, t.name as theme_name, t.description, t.thumbnail "
                + "FROM reservation AS r "
                + "INNER JOIN member AS m ON r.member_id = m.id "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id "
                + "WHERE r.id = ?";
        try {
            Reservation reservation = template.queryForObject(sql, mapper, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean checkAlreadyReserved(LocalDate date, long timeId, long themeId) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "INNER JOIN theme AS t ON r.theme_id = t.id "
                + "WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?)";
        return template.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    public boolean checkExistenceInTime(long reservationTimeId) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS rt ON r.time_id = rt.id "
                + "WHERE r.time_id = ?)";
        return template.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    public boolean checkExistenceInTheme(long themeId) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation AS r "
                + "INNER JOIN theme AS t ON r.theme_id = t.id "
                + "WHERE r.theme_id = ?)";
        return template.queryForObject(sql, Boolean.class, themeId);
    }

    public long add(Reservation reservation) {
        insertReservation = initializeSimpleJdbcInsert();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        parameters.put("member_id", reservation.getMember().getId());
        return insertReservation.executeAndReturnKey(parameters).longValue();
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE reservation.id = ?";
        template.update(sql, id);
    }

    private SimpleJdbcInsert initializeSimpleJdbcInsert() {
        if (insertReservation == null) {
            this.insertReservation = new SimpleJdbcInsert(template)
                    .withTableName("reservation")
                    .usingGeneratedKeyColumns("id");
        }
        return insertReservation;
    }
}

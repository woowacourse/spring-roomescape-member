package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.Role;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role"))
            ),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            ),
            resultSet.getTimestamp("created_at").toLocalDateTime()
    );
    private final String basicSelectQuery = "SELECT " +
            "r.id AS reservation_id, " +
            "r.date, " +
            "r.created_at, " +
            "m.id AS member_id, " +
            "m.name AS member_name, " +
            "m.email AS email, " +
            "m.password AS password, " +
            "m.role AS role, " +
            "t.id AS time_id, " +
            "t.start_at AS time_value, " +
            "th.id AS theme_id, " +
            "th.name AS theme_name, " +
            "th.description AS theme_description, " +
            "th.thumbnail AS theme_thumbnail " +
            "FROM reservation AS r " +
            "INNER JOIN reservation_time AS t ON r.time_id = t.id " +
            "INNER JOIN theme AS th ON r.theme_id = th.id " +
            "INNER JOIN member AS m ON r.member_id = m.id ";

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query(this.basicSelectQuery, reservationRowMapper);

        return Collections.unmodifiableList(reservations);
    }

    public Reservation findById(Long id) {
        String sql = basicSelectQuery + "WHERE reservation_id = ?";
        Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        if (reservation == null) {
            throw new NoSuchElementException("존재하지 않는 아아디입니다.");
        }

        return reservation;
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
                "member_id", reservation.getMember().getId(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId(),
                "created_at", reservation.getCreatedAt()
        );
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getMember(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme(), reservation.getCreatedAt());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existByReservationTimeId(Long id) {
        String sql = basicSelectQuery + "WHERE time_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);

        return !reservations.isEmpty();
    }

    public boolean existByThemeId(Long id) {
        String sql = basicSelectQuery + "WHERE theme_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);

        return !reservations.isEmpty();
    }

    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = basicSelectQuery + "WHERE r.date = ? AND t.id = ? AND th.id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, date, timeId, themeId);

        return !reservations.isEmpty();
    }

    public boolean existByMemberId(Long id) {
        String sql = basicSelectQuery + "WHERE member_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);

        return !reservations.isEmpty();
    }

    public List<Reservation> findByFilter(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = basicSelectQuery + "WHERE " +
                "member_id = ? " +
                "AND theme_id = ? " +
                "AND r.date >= ? " +
                "AND r.date <= ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, memberId, themeId, dateFrom,
                dateTo);

        return Collections.unmodifiableList(reservations);
    }
}

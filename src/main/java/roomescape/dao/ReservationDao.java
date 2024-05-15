package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationDetailRequest;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getObject("date", LocalDate.class),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("email"),
                        Role.valueOf(resultSet.getString("role"))),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getObject("start_at", LocalTime.class)),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail"))
        );
    }

    public List<Reservation> readReservations() {
        String sql = """
                SELECT reservation.id, reservation.date,
                        reservation.member_id, member.name AS member_name, member.email, member.role,
                        reservation.time_id, reservation_time.start_at,
                        reservation.theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                FROM reservation
                JOIN member ON reservation.member_id = member.id
                JOIN reservation_time ON reservation.time_id = reservation_time.id
                JOIN theme ON reservation.theme_id = theme.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Reservation> readReservationsByDetails(ReservationDetailRequest request) {
        String sql = """
                SELECT reservation.id, reservation.date,
                        reservation.member_id, member.name AS member_name, member.email, member.role,
                        reservation.time_id, reservation_time.start_at,
                        reservation.theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                FROM reservation
                JOIN member ON reservation.member_id = member.id
                JOIN reservation_time ON reservation.time_id = reservation_time.id
                JOIN theme ON reservation.theme_id = theme.id
                WHERE reservation.theme_id = ? AND reservation.member_id = ? AND reservation.date >= ? AND reservation.date <= ?
                """;
        return jdbcTemplate.query(sql, rowMapper, request.themeId(), request.memberId(), request.dateFrom(), request.dateTo());
    }

    private Optional<Reservation> readReservationById(Long id) {
        String sql = """
                SELECT reservation.id, reservation.date,
                        reservation.member_id, member.name AS member_name, member.email, member.role,
                        reservation.time_id, reservation_time.start_at,
                        reservation.theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                FROM reservation
                JOIN member ON reservation.member_id = member.id
                JOIN reservation_time ON reservation.time_id = reservation_time.id
                JOIN theme ON reservation.theme_id = theme.id
                WHERE reservation.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean existsReservationByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public boolean existsReservationByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    public boolean existsReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ? AND time_id = ? AND theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    public Reservation createReservation(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation (date, member_id, time_id, theme_id) values (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setObject(1, reservation.getDate());
            preparedStatement.setLong(2, reservation.getMemberId());
            preparedStatement.setLong(3, reservation.getTimeId());
            preparedStatement.setLong(4, reservation.getThemeId());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return readReservationById(id).orElseThrow(() -> new IllegalStateException("예약이 제대로 생성되지 않아 조회할 수 없습니다."));
    }

    public void deleteReservation(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

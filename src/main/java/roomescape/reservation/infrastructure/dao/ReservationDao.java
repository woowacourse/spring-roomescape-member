package roomescape.reservation.infrastructure.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.exception.DeleteReservationException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation insert(final CreateReservationRequest request) {
        String sql = "insert into reservation (member_id, theme_id, date, time_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setLong(1, request.getMember().getId());
            ps.setLong(2, request.getTheme().getId());
            ps.setString(3, request.getDate().getReservationDate().toString());
            ps.setLong(4, request.getTime().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();

        return new Reservation(id, request.getMember(), request.getTheme(), request.getDate(), request.getTime());
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = getSelectAllQuery();
        return jdbcTemplate.query(sql, mapToReservation());
    }

    @Override
    public void delete(final Long id) {
        String sql = "delete from reservation where id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows != 1) {
            throw new DeleteReservationException("[ERROR] 삭제하지 못했습니다.");
        }
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return result == 1;
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return result == 1;
    }

    @Override
    public boolean existsByDateTime(LocalDateTime reservationDateTime) {
        LocalDate date = reservationDateTime.toLocalDate();
        LocalTime time = reservationDateTime.toLocalTime();
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    INNER JOIN reservation_time rt
                    ON r.time_id = rt.id
                    WHERE r.date = ?
                    AND rt.start_at = ?
                )
                """;

        int result = jdbcTemplate.queryForObject(sql, Integer.class, date.toString(), time.toString());
        return result == 1;
    }

    @Override
    public boolean existsByDateAndThemeIdAndTimeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.date = ?
                    AND r.time_id = ?
                    AND r.theme_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return result == 1;
    }

    @Override
    public List<Reservation> findAllByFilters(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        StringBuilder sql = new StringBuilder(getSelectAllQuery());
        List<Object> parameters = new ArrayList<>();
        List<String> whereClauses = new ArrayList<>();

        addThemeFilter(themeId, whereClauses, parameters);
        addMemberFilter(memberId, whereClauses, parameters);
        addDateFilter(dateFrom, dateTo, whereClauses, parameters);

        if (!whereClauses.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", whereClauses));
        }

        return jdbcTemplate.query(sql.toString(), parameters.toArray(), mapToReservation());
    }

    private void addThemeFilter(Long themeId, List<String> whereClauses, List<Object> parameters) {
        if (themeId != null) {
            whereClauses.add("t.id = ? ");
            parameters.add(themeId);
        }
    }

    private void addMemberFilter(Long memberId, List<String> whereClauses, List<Object> parameters) {
        if (memberId != null) {
            whereClauses.add("m.id = ? ");
            parameters.add(memberId);
        }
    }

    private void addDateFilter(LocalDate dateFrom, LocalDate dateTo, List<String> whereClauses,
                               List<Object> parameters) {
        if (dateFrom != null) {
            whereClauses.add("r.date >= ? ");
            parameters.add(dateFrom);
        }

        if (dateTo != null) {
            whereClauses.add("r.date <= ? ");
            parameters.add(dateTo);
        }
    }

    private RowMapper<Reservation> mapToReservation() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("r_id"),
                new Member(
                        resultSet.getLong("m_id"),
                        resultSet.getString("m_name"),
                        resultSet.getString("m_email"),
                        resultSet.getString("m_password"),
                        Role.valueOf(resultSet.getString("m_role"))
                ),
                new Theme(
                        resultSet.getLong("t_id"),
                        resultSet.getString("t_name"),
                        resultSet.getString("t_description"),
                        resultSet.getString("t_thumbnail")
                ),
                new ReservationDate(
                        resultSet.getDate("r_date").toLocalDate()
                ),
                new ReservationTime(
                        resultSet.getLong("rt_id"),
                        resultSet.getTime("rt_start_at").toLocalTime()
                )
        );
    }

    private String getSelectAllQuery() {
        return """
                SELECT
                    r.id as r_id,
                    r.date as r_date,
                    rt.id as rt_id,
                    rt.start_at as rt_start_at,
                    t.id as t_id,
                    t.name as t_name,
                    t.description as t_description,
                    t.thumbnail as t_thumbnail,
                    m.id as m_id,
                    m.name as m_name,
                    m.email as m_email,
                    m.password as m_password,
                    m.role as m_role
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme t ON r.theme_id = t.id
                INNER JOIN member m ON m.id = r.member_id 
                """;
    }
}

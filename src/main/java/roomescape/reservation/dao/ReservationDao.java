package roomescape.reservation.dao;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.dto.ReservationMember;
import roomescape.reservation.domain.repository.ReservationRepository;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleReservationInsert;
    private final SimpleJdbcInsert simpleMemberReservationInsert;

    private final RowMapper<ReservationMember> rowMapper = (ResultSet resultSet, int rowNum) -> {
        return new ReservationMember(
                resultSet.getLong("member_reservation_id"),
                new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getDate("date").toLocalDate(),
                        new ReservationTime(resultSet.getLong("time_id"),
                                resultSet.getTime("time_value").toLocalTime()
                        ),
                        new Theme(resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail"))
                ),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name")
                )
        );
    };

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleReservationInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.simpleMemberReservationInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member_reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        long id = simpleReservationInsert.executeAndReturnKey(params).longValue();
        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<ReservationMember> findAllMemberReservation() {
        String sql = """
                SELECT mr.id AS member_reservation_id, r.id AS reservation_id, r.date, t.id AS time_id, t.start_at AS time_value, 
                th.id AS theme_id, th.name AS theme_name, th.description, th.thumbnail, m.id AS member_id, m.name AS member_name 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id 
                INNER JOIN theme AS th ON r.theme_id = th.id 
                INNER JOIN member_reservation AS mr ON mr.reservation_id = r.id 
                INNER JOIN member AS m ON m.id = mr.member_id;
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean deleteMemberReservationById(long memberReservationId) {
        String sql = "DELETE FROM member_reservation WHERE id = ?;";
        int updateId = jdbcTemplate.update(sql, memberReservationId);
        return updateId != 0;
    }

    @Override
    public void deleteMemberReservationByReservationId(long reservationId) {
        String sql = "DELETE FROM member_reservation WHERE reservation_id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    @Override
    public boolean delete(final long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        int updateId = jdbcTemplate.update(sql, reservationId);
        return updateId != 0;
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String sql = """
                SELECT 1
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON r.theme_id = th.id 
                WHERE t.id = ? 
                LIMIT 1;
                """;

        return jdbcTemplate.query(sql, ResultSet::next, timeId);
    }

    @Override
    public boolean existsByThemeId(final long themeId) {
        String sql = """
                SELECT 1
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON r.theme_id = th.id 
                WHERE th.id = ? 
                LIMIT 1;
                """;

        return jdbcTemplate.query(sql, ResultSet::next, themeId);
    }

    @Override
    public boolean existsBy(final LocalDate date, final long timeId, final long themeId) {
        String sql = """
                SELECT 1
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id 
                INNER JOIN theme as th ON r.theme_id = th.id 
                WHERE date = ? AND time_id = ? AND theme_id = ?
                LIMIT 1;
                """;

        return jdbcTemplate.query(sql, ResultSet::next, date, timeId, themeId);
    }

    @Override
    public boolean existMemberReservationBy(LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT 1
                FROM member_reservation as mr
                WHERE mr.reservation_id = (
                    SELECT r.id
                    FROM reservation AS r 
                    INNER JOIN reservation_time AS t ON r.time_id = t.id 
                    INNER JOIN theme AS th ON r.theme_id = th.id 
                    INNER JOIN member_reservation AS mr ON mr.reservation_id = r.id 
                    INNER JOIN member AS m ON m.id = mr.member_id 
                    WHERE date = ? AND time_id = ? AND theme_id = ?
                    LIMIT 1
                );
                """;

        return jdbcTemplate.query(sql, ResultSet::next, date, timeId, themeId);
    }

    @Override
    public long saveMemberReservation(long memberId, long reservationId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("reservation_id", reservationId);
        return simpleMemberReservationInsert.executeAndReturnKey(params).longValue();
    }
}

package roomescape.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation insert(Reservation reservation) {
        Member member = reservation.getMember();
        Long memberId = member.getId();
        LocalDate date = reservation.getDate();
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("date", date)
                .addValue("time_id", time.getId())
                .addValue("theme_id", theme.getId());

        long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Reservation(id, member, date, time, theme);
    }

    public List<Reservation> selectAll() {
        String sql =
                "SELECT " +
                        "r.id AS reservation_id, " +
                        "m.id AS member_id, " +
                        "m.name AS member_name, " +
                        "m.email AS member_email, " +
                        "m.password AS member_password, " +
                        "r.date, " +
                        "rt.id AS time_id, " +
                        "rt.start_at AS time_value, " +
                        "t.id AS theme_id, " +
                        "t.name AS theme_name, " +
                        "t.description AS theme_description, " +
                        "t.thumbnail AS theme_thumbnail " +
                        "FROM reservation AS r " +
                        "INNER JOIN reservation_time AS rt " +
                        "ON r.time_id = rt.id " +
                        "INNER JOIN theme AS t " +
                        "ON r.theme_id = t.id " +
                        "INNER JOIN member AS m " +
                        "ON r.member_id = m.id";

        return jdbcTemplate.query(sql, reservationRowMapper());
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean hasReservationTime(Long timeId) {
        String sql =
                "SELECT EXISTS (" +
                        "    SELECT 1 " +
                        "    FROM reservation " +
                        "    WHERE time_id = ?" +
                        ")";
        try {
            return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new queryResultSizeException("db 쿼리 조회 에러");
        }
    }

    public List<Long> findReservedTimeIds(LocalDate date, Long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("time_id"), date, themeId);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                new Member(resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        resultSet.getString("member_password")),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
    }
}

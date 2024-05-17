package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.exceptions.ExistingEntryException;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;
import roomescape.member.domain.Password;
import roomescape.member.domain.Role;
import roomescape.member.domain.UserName;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {
    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            ),
            new Member(
                    resultSet.getLong("member_id"),
                    new UserName(resultSet.getString("member_name")),
                    new Email(resultSet.getString("email")),
                    new Password(resultSet.getString("password")),
                    Role.valueOf(resultSet.getString("role"))
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role
                FROM
                    reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m on r.member_id = m.id
                ORDER BY id;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findByReservationId(Long id) {
        String sql = """
                SELECT
                    r.id AS id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role
                FROM
                    reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m on r.member_id = m.id
                WHERE
                    r.id = ?;
                """;
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role
                FROM
                    reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m on r.member_id = m.id
                WHERE date = ? AND theme_id = ?;
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, date, themeId);
    }

    public List<Reservation> findByIdsAndDates(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    r.id AS id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role
                FROM
                    reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m on r.member_id = m.id
                WHERE TRUE
                """;
        String conditions = makeQueryByParams(memberId, themeId, from, to);
        return jdbcTemplate.query(sql + conditions, reservationRowMapper);
    }

    private String makeQueryByParams(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = "";

        if (memberId != null) {
            sql += "AND member_id = " + memberId + " ";
        }
        if (themeId != null) {
            sql += "AND theme_id = " + themeId + " ";
        }
        if (dateFrom != null) {
            sql += "AND r.date >= '" + dateFrom + "' ";
        }
        if (dateTo != null) {
            sql += "AND r.date <= '" + dateTo + "'";
        }
        sql += ";";
        return sql;
    }

    public Reservation save(Reservation reservation) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("date", reservation.getDate())
                    .addValue("time_id", reservation.getReservationTime().getId())
                    .addValue("theme_id", reservation.getTheme().getId())
                    .addValue("member_id", reservation.getMember().getId());
            Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
            return new Reservation(
                    id,
                    reservation.getDate(),
                    reservation.getReservationTime(),
                    reservation.getTheme(),
                    reservation.getMember()
            );
        } catch (DuplicateKeyException e) {
            String dateTime = reservation.getDate() + " " + reservation.getReservationTime().getStartAt();
            throw new ExistingEntryException(dateTime + "은 이미 예약된 시간입니다.");
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

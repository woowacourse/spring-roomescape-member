package roomescape.infrastructure.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Description;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.MemberName;
import roomescape.domain.MemberRole;
import roomescape.domain.Password;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.Thumbnail;
import roomescape.infrastructure.persistence.dynamic.ReservationQueryConditions;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("member_id", reservation.getMemberId())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(
                id,
                reservation.getDate(),
                reservation.getMember(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public void removeById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean hasBy(ReservationQueryConditions conditions) {
        String sql = """
                SELECT count(*)
                FROM reservation
                """;

        int hasCount = jdbcTemplate.queryForObject(
                conditions.createDynamicQuery(sql),
                conditions.getArgs(),
                conditions.getArgTypes(),
                Integer.class
        );

        return hasCount > 0;
    }

    public List<Reservation> findAllBy(ReservationQueryConditions conditions) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail,
                    m.id AS member_id,
                    m.name,
                    m.role,
                    m.email,
                    m.password
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                INNER JOIN member AS m
                ON r.member_id = m.id
                """;

        return jdbcTemplate.query(
                conditions.createDynamicQuery(sql),
                conditions.getArgs(),
                conditions.getArgTypes(),
                getReservationRowMapper()
        );
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));
            return new Reservation(
                    resultSet.getLong("reservation_id"),
                    LocalDate.parse(resultSet.getString("date")),
                    new Member(
                            resultSet.getLong("member_id"),
                            new MemberName(resultSet.getString("name")),
                            new Email(resultSet.getString("email")),
                            new Password(resultSet.getString("password")),
                            MemberRole.from(resultSet.getString("role"))
                    ),
                    new ReservationTime(resultSet.getLong("time_id"), startAt),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            new ThemeName(resultSet.getString("theme_name")),
                            new Description(resultSet.getString("description")),
                            new Thumbnail(resultSet.getString("thumbnail"))
                    )
            );
        };
    }
}

package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationMapper = (resultSet, rowNum) -> {
        ReservationTime time = ReservationTime.load(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class)
        );

        Theme theme = Theme.load(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );

        Member member = new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                resultSet.getString("member_password"),
                Role.USER
        );

        return Reservation.load(
                resultSet.getLong("reservation_id"),
                resultSet.getObject("date", LocalDate.class),
                member,
                time,
                theme
        );
    };

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member m 
                    ON r.member_id = m.id
                """;
        return jdbcTemplate.query(sql, reservationMapper);
    }

    @Override
    public Reservation create(final Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(Map.of(
                "date", reservation.getDate(),
                "member_id", reservation.getMember().getId(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()));
        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        return Reservation.load(key.longValue(), reservation.getDate(), reservation.getMember(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void delete(final Long id) {
        final String sql = "DELETE reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Reservation> findByThemeAndDateAndTime(final Reservation reservation) {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member m 
                    ON r.member_id = m.id
                WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationMapper, reservation.getDate(),
                reservation.getTime().getId(), reservation.getTheme().getId()).stream().findFirst();
    }

    @Override
    public Optional<Reservation> findByTimeId(final Long timeId) {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member m 
                    ON r.member_id = m.id
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, reservationMapper, timeId).stream().findFirst();
    }

    @Override
    public Optional<Reservation> findByThemeId(final Long themeId) {
        final String sql = """
                SELECT
                    r.id as reservation_id,
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme th
                    ON r.theme_id = th.id
                INNER JOIN member m 
                    ON r.member_id = m.id
                WHERE r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationMapper, themeId).stream().findFirst();
    }

    @Override
    public boolean existById(final Long id) {
        final String sql = """
                SELECT EXISTS (
                  SELECT 1
                  FROM reservation
                  WHERE id = ?
                ) AS is_exist;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }
}

package roomescape.infrastructure;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password")
            ),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        return new Reservation(id, reservation);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    mem.id AS member_id,
                    mem.name AS member_name,
                    mem.email AS member_email,
                    mem.password AS member_password,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                    INNER JOIN member AS mem
                        ON r.member_id = mem.id""";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        try {
            String sql = """
                    SELECT
                        r.id AS reservation_id,
                        mem.id AS member_id,
                        mem.name AS member_name,
                        mem.email AS member_email,
                        mem.password AS member_password,
                        r.date,
                        t.id AS time_id,
                        t.start_at AS start_at,
                        th.id AS theme_id,
                        th.name AS theme_name,
                        th.description AS theme_description,
                        th.thumbnail AS theme_thumbnail
                    FROM reservation AS r
                        INNER JOIN reservation_time AS t
                            ON r.time_id = t.id
                        INNER JOIN theme AS th
                            ON r.theme_id = th.id
                        INNER JOIN member AS mem
                            ON r.member_id = mem.id
                    WHERE r.id = ?""";
            Reservation reservation = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByDateBetween(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    mem.id AS member_id,
                    mem.name AS member_name,
                    mem.email AS member_email,
                    mem.password AS member_password,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                    INNER JOIN member AS mem
                        ON r.member_id = mem.id
                WHERE r.date BETWEEN ? AND ?""";
        return jdbcTemplate.query(sql, rowMapper, start, end);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    mem.id AS member_id,
                    mem.name AS member_name,
                    mem.email AS member_email,
                    mem.password AS member_password,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                    INNER JOIN member AS mem
                        ON r.member_id = mem.id
                WHERE r.date = ? AND th.id = ?""";
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation WHERE time_id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
        return Boolean.TRUE.equals(isExist);
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation WHERE theme_id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
        return Boolean.TRUE.equals(isExist);
    }

    @Override
    public boolean existsBy(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(isExist);
    }

    @Override
    public List<Reservation> findByMemberAndThemeAndDateBetween(
            long memberId, long themeId, LocalDate startDate, LocalDate endDate
    ) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    mem.id AS member_id,
                    mem.name AS member_name,
                    mem.email AS member_email,
                    mem.password AS member_password,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r
                    INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                    INNER JOIN theme AS th
                        ON r.theme_id = th.id
                    INNER JOIN member AS mem
                        ON r.member_id = mem.id""";
        String filterCondition = " WHERE MEMBER_ID = ? AND THEME_ID = ? AND DATE BETWEEN ? AND ?";
        return jdbcTemplate.query(
                sql + filterCondition, rowMapper,
                memberId, themeId, startDate, endDate
        );
    }
}

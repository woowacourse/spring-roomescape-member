package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.ReservationRowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomBadRequest;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper rowMapper;
    private final String BASE_SQL = """
                                    SELECT
                                    r.id AS reservation_id,
                                    r.date,
                                    t.id AS time_id,
                                    t.start_at,
                                    th.id AS theme_id,
                                    th.name AS theme_name,
                                    th.description AS theme_description,
                                    th.thumbnail AS theme_thumbnail,
                                    m.id AS member_id,
                                    m.name AS member_name,
                                    m.email AS member_email,
                                    m.password AS member_password,
                                    m.role AS member_role
                                    FROM reservation AS r
                                    INNER JOIN reservation_time AS t
                                    ON r.time_id = t.id
                                    INNER JOIN theme AS th
                                    ON r.theme_id = th.id
                                    INNER JOIN member AS m
                                    ON r.member_id = m.id
                                    """;

    public ReservationDao(final JdbcTemplate jdbcTemplate,
                          final DataSource dataSource,
                          final ReservationRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Reservation create(final Reservation reservation) {
        final var params = new MapSqlParameterSource()
                .addValue("date", reservation.getDateAsString())
                .addValue("time_id", reservation.getTime().id())
                .addValue("theme_id", reservation.getTheme().id())
                .addValue("member_id", reservation.getMember().id());
        try {
            final var id = jdbcInsert.executeAndReturnKey(params).longValue();
            return Reservation.of(id, reservation);
        } catch (final DuplicateKeyException error) {
            throw new CustomBadRequest("중복 예약은 불가능합니다.");
        }
    }

    public List<Reservation> filter(final Long themeId,
                                    final Long memberId,
                                    final String dateFromValue,
                                    final String dateToValue) {
        final var dateFrom = dateFromValue == null || dateFromValue.isEmpty() ? null : dateFromValue;
        final var dateTo = dateToValue == null || dateToValue.isEmpty() ? null : dateToValue;
        final var sql = BASE_SQL + """
                                    WHERE (? IS NULL OR theme_id = ?) AND (? IS NULL OR member_id = ?)
                                    AND (
                                        (? IS NULL AND ? IS NULL)
                                        OR (? IS NULL AND date <= ?)
                                        OR (? IS NULL AND date >= ?)
                                        OR (date BETWEEN ? AND ?)
                                    )
                                    """;
        return jdbcTemplate.query(
                sql, rowMapper,
                themeId, themeId,
                memberId, memberId,
                dateFrom, dateTo,
                dateFrom, dateTo,
                dateTo, dateFrom,
                dateFrom, dateTo
        );
    }

    public Optional<Reservation> findById(final long reservationId) {
        final String sql = BASE_SQL + "WHERE r.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, reservationId));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean exists(final ReservationTime time) {
        return existsByCondition("time_id", time.id());
    }

    public boolean exists(final Theme theme) {
        return existsByCondition("theme_id", theme.id());
    }

    private boolean existsByCondition(final String conditionColumn, final Object conditionValue) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE " + conditionColumn + " = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, conditionValue));
    }

    public void delete(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

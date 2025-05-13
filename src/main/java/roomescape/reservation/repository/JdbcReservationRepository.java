package roomescape.reservation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.DataExistException;
import roomescape.common.exception.SaveException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    new Member(
                            rs.getLong("member_id"),
                            rs.getString("member_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))
                    ),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    ),
                    new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    )
            );

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Long save(final Reservation reservation) {
        String sql = "INSERT INTO reservations (member_id, date, time_id, theme_id) VALUES (:memberId, :date, :timeId, :themeId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", reservation.getMember().getId())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());

        final int rowAffected = namedParameterJdbcTemplate.update(sql, params, keyHolder);

        if (rowAffected != 1) {
            throw new SaveException("예약 정보 저장에 실패했습니다.");
        }

        final Number key = keyHolder.getKey();
        return key.longValue();
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM reservations WHERE id = :id";
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", id);
            namedParameterJdbcTemplate.update(sql, params);
        } catch (final DataIntegrityViolationException e) {
            throw new DataExistException("데이터 무결성 제약으로 인해 삭제할 수 없습니다." + e);
        }
    }

    @Override
    public boolean existsByDateAndStartAtAndThemeId(final LocalDate date, final LocalTime startAt, final Long themeId) {
        final String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservations AS r
                    INNER JOIN reservation_times AS rt
                    ON r.time_id = rt.id
                    INNER JOIN themes AS th
                    ON r.theme_id = th.id
                    WHERE r.date = :date AND rt.start_at = :startAt AND th.id = :themeId
                )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("startAt", startAt)
                .addValue("themeId", themeId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        final String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservations AS r
                    WHERE r.date = :date AND r.time_id = :timeId AND r.theme_id = :themeId
                    LIMIT 1
                )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.date AS date,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM reservations AS r
                INNER JOIN reservation_times AS rt
                ON r.time_id = rt.id
                INNER JOIN themes AS th
                ON r.theme_id = th.id
                INNER JOIN members AS m
                ON r.member_id = m.id
                WHERE r.id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        final List<Reservation> reservations = namedParameterJdbcTemplate.query(sql, params, RESERVATION_ROW_MAPPER);
        if (!reservations.isEmpty()) {
            return Optional.of(reservations.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.date AS date,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM reservations AS r
                INNER JOIN reservation_times AS rt
                ON r.time_id = rt.id
                INNER JOIN themes AS th
                ON r.theme_id = th.id
                INNER JOIN members AS m
                ON r.member_id = m.id
                """;

        return namedParameterJdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    @Override
    public List<Reservation> findByInFromTo(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                            final LocalDate dateTo) {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.date AS date,
                    m.id AS member_id,
                    m.name AS member_name,
                    m.email AS email,
                    m.password AS password,
                    m.role AS role,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM reservations AS r
                INNER JOIN reservation_times AS rt
                ON r.time_id = rt.id
                INNER JOIN themes AS th
                ON r.theme_id = th.id
                INNER JOIN members AS m
                ON r.member_id = m.id
                WHERE ((:dateFrom IS NULL OR :dateTo IS NULL) OR (r.date BETWEEN :dateFrom AND :dateTo))
                AND (th.id = :themeId OR :themeId IS NULL)
                AND (m.id = :memberId OR :memberId IS NULL)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("dateFrom", dateFrom)
                .addValue("dateTo", dateTo)
                .addValue("themeId", themeId)
                .addValue("memberId", memberId);

        return namedParameterJdbcTemplate.query(sql, params, RESERVATION_ROW_MAPPER);
    }
}

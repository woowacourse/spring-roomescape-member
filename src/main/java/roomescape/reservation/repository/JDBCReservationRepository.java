package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.entity.MemberEntity;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

@Repository
public class JDBCReservationRepository implements ReservationRepository {

    private static final String SELECT_ALL_RESERVATION_WITH_JOIN = "SELECT "
            + "r.id as reservation_id, "
            + "r.date, "
            + "t.id as time_id, "
            + "t.start_at as time_value, "
            + "th.id as theme_id, "
            + "th.name as theme_name, "
            + "th.description, "
            + "th.thumbnail, "
            + "m.id as member_id, "
            + "m.name as member_name, "
            + "m.email as member_email, "
            + "m.password as member_password, "
            + "m.role as member_role "
            + "FROM reservation as r "
            + "inner join reservation_time as t "
            + "on r.time_id = t.id "
            + "inner join theme as th "
            + "on r.theme_id = th.id "
            + "inner join member as m "
            + "on r.member_id = m.id";

    private static final String SELECT_FILTERED_RESERVATION_WITH_JOIN = "SELECT "
            + "r.id as reservation_id, "
            + "r.date, "
            + "t.id as time_id, "
            + "t.start_at as time_value, "
            + "th.id as theme_id, "
            + "th.name as theme_name, "
            + "th.description, "
            + "th.thumbnail, "
            + "m.id as member_id, "
            + "m.name as member_name, "
            + "m.email as member_email, "
            + "m.password as member_password, "
            + "m.role as member_role "
            + "FROM reservation as r "
            + "inner join reservation_time as t "
            + "on r.time_id = t.id "
            + "inner join theme as th "
            + "on r.theme_id = th.id and r.theme_id = ? "
            + "inner join member as m "
            + "on r.member_id = m.id and r.member_id = ? "
            + "WHERE PARSEDATETIME(r.date, 'yyyy-MM-dd') between ? and ? ";

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> {
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                resultSet.getLong("time_id"),
                resultSet.getString("time_value"));

        ThemeEntity themeEntity = new ThemeEntity(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));

        MemberEntity memberEntity = new MemberEntity(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                resultSet.getString("member_password"),
                resultSet.getString("member_role")
        );

        ReservationEntity entity = new ReservationEntity(
                resultSet.getLong("reservation_id"),
                resultSet.getString("date"),
                memberEntity,
                timeEntity,
                themeEntity
        );
        return entity.toReservation();
    };
    private static final String RESERVATION_TIME_ALREADY_BOOKED = "SELECT rt.id, rt.start_at, "
            + "CASE WHEN r.id IS NOT NULL THEN true ELSE false END AS already_booked "
            + "FROM reservation_time AS rt "
            + "LEFT JOIN ( "
            + "    SELECT time_id, id FROM reservation "
            + "    WHERE date = ? AND theme_id = ? "
            + ") r ON rt.id = r.time_id "
            + "ORDER BY rt.start_at";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                SELECT_ALL_RESERVATION_WITH_JOIN,
                RESERVATION_ROW_MAPPER
        );
    }

    @Override
    public List<Reservation> findFilteredReservations(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                                      final LocalDate dateTo) {
        return jdbcTemplate.query(
                SELECT_FILTERED_RESERVATION_WITH_JOIN,
                RESERVATION_ROW_MAPPER,
                themeId, memberId, dateFrom, dateTo
        );
    }

    @Override
    public Reservation save(final Reservation reservation) {
        Long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("date", reservation.getDate(), "time_id", reservation.getTime().getId(),
                        "theme_id", reservation.getTheme().getId(), "member_id", reservation.getMember().getId())
        ).longValue();

        return Reservation.of(generatedId, reservation.getDate(), reservation.getMember(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public boolean deleteById(final Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id) != 0;
    }

    @Override
    public boolean existsByTimeId(final Long id) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)",
                    Boolean.class,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean existsByThemeId(final Long id) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)",
                    Boolean.class,
                    id
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM reservation WHERE (date, time_id, theme_id) = (?, ?, ?))",
                    Boolean.class,
                    date,
                    timeId,
                    themeId
            ));
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public List<AvailableReservationTimeResponse> findAvailableTimesByDateAndThemeId(final LocalDate date,
                                                                                     final Long themeId) {
        return jdbcTemplate.query(RESERVATION_TIME_ALREADY_BOOKED, (rs, rowNum) -> new AvailableReservationTimeResponse(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at")),
                rs.getBoolean("already_booked")
        ), date, themeId);
    }
}

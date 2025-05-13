package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.application.dto.ReservationInfoResponse;
import roomescape.domain.model.Member;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final RowMapper<ReservationInfoResponse> ROW_MAPPER = (resultSet, rowNum) -> {
        Member member = new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("role"));
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_value").toLocalTime()
        );
        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );
        return new ReservationInfoResponse(
                resultSet.getLong("id"),
                member,
                resultSet.getDate("date").toLocalDate(),
                reservationTime,
                theme
        );
    };

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationInfoResponse> findAll() {
        String query = """
                   SELECT r.id, 
                   m.id AS member_id, m.name AS member_name, m.email AS member_email, m.role AS member_role,
                   r.date,
                   rt.id AS time_id, rt.start_at AS time_value,
                   t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, t.thumbnail AS theme_thumbnail
                   FROM reservation AS r
                   INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                   INNER JOIN theme AS t ON r.theme_id = t.id
                   INNER JOIN member AS m ON r.member_id = m.id
                """;
        return jdbcTemplate.query(
                query,
                ROW_MAPPER
        );
    }

    @Override
    public List<ReservationInfoResponse> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        String query = """
                   SELECT r.id,
                   m.id AS member_id, m.name AS member_name, m.email AS member_email, m.role AS member_role,
                   r.date,
                   rt.id AS time_id, rt.start_at AS time_value,
                   t.id AS theme_id, t.name AS theme_name, t.description AS theme_description, t.thumbnail AS theme_thumbnail
                   FROM reservation AS r
                   INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                   INNER JOIN theme AS t ON r.theme_id = t.id
                   INNER JOIN member AS m ON r.member_id = m.id
                   WHERE t.id = ? AND r.member_id = ? AND PARSEDATETIME(r.date, 'yyyy-MM-dd') BETWEEN PARSEDATETIME(?, 'yyyy-MM-dd') AND PARSEDATETIME(?, 'yyyy-MM-dd')
                """;
        return jdbcTemplate.query(
                query,
                ROW_MAPPER,
                themeId,
                memberId,
                dateFrom,
                dateTo
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", reservation.getMemberId());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(
                id,
                reservation.getMemberId(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId);
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, themeId);
    }

    @Override
    public boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        String query = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ? and theme_id = ? and date = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, timeId, themeId, date);
    }

    @Override
    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        String query = "SELECT time_id FROM reservation WHERE theme_id = ? and date = ?";
        return jdbcTemplate.query(query, (resultSet, row) -> resultSet.getLong(1), themeId, date);
    }
}

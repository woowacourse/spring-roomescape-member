package roomescape.reservation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class ReservationDao {

    private static final String TABLE_NAME = "reservation";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                select rs.id as reservation_id, rs.date,
                       m.id as member_id, m.name, m.email, m.password, m.role,
                       rt.id as reservation_time_id, rt.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                inner join reservation_time rt ON rs.time_id = rt.id
                inner join theme th ON rs.theme_id = th.id
                inner join member m ON rs.member_id = m.id
                """;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = """
                select rs.id as reservation_id, rs.date,
                    m.id as member_id, m.name, m.email, m.password, m.role,
                    rt.id as reservation_time_id, rt.start_at,
                    th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                inner join reservation_time rt on rs.time_id = rt.id
                inner join theme th on rs.theme_id = th.id
                inner join member m on rs.member_id = m.id
                where th.id = :theme_id and rs.date = :date
                """;

        Map<String, Object> params = Map.of("theme_id", themeId, "date", date);

        return jdbcTemplate.query(sql, params,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    public Optional<Reservation> findById(final Long id) {
        String sql = """
                select rs.id as reservation_id, rs.date,
                       m.id as member_id, m.name, m.email, m.password, m.role,
                       rt.id as reservation_time_id, rt.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                inner join reservation_time rt ON rs.time_id = rt.id
                inner join theme th ON rs.theme_id = th.id
                inner join member m ON rs.member_id = m.id
                where rs.id = :reservation_id
                """;

        Map<String, Long> params = Map.of("reservation_id", id);

        return reservationOf(sql, params);
    }

    public Reservation save(final Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("member_id", reservation.getMember().getId());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getMember(), reservation.getReservationDate(),
                reservation.getReservationTime(), reservation.getTheme());
    }

    public int deleteById(final Long id) {
        String sql = "delete from reservation where id = :id";
        Map<String, Long> params = Map.of("id", id);

        return jdbcTemplate.update(sql, params);
    }

    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        String sql = """
                select count(*)
                from reservation
                where date = :date and time_id = :time_id and theme_id = :theme_id
                """;
        Map<String, Object> params = Map.of("date", date, "time_id", timeId, "theme_id", themeId);

        int rowCountByDateAndTimeAndTheme = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return rowCountByDateAndTimeAndTheme == 1;
    }

    public boolean existsByTimeId(final Long timeId) {
        String selectSql = "select count(*) from reservation where time_id = :time_id";
        Map<String, Long> params = Map.of("time_id", timeId);

        int rowCountByTimeId = jdbcTemplate.queryForObject(selectSql, params, Integer.class);

        return rowCountByTimeId > 0;
    }

    public boolean existsByThemeId(final Long themeId) {
        String selectSql = "select count(*) from reservation where theme_id = :theme_id";

        Map<String, Long> params = Map.of("theme_id", themeId);

        int rowCountByThemeId = jdbcTemplate.queryForObject(selectSql, params, Integer.class);

        return rowCountByThemeId > 0;
    }

    public List<Reservation> findByThemeIdAndMemberIdAndBetweenDate(
            final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo
    ) {
        String sql = """
                select rs.id as reservation_id, rs.date,
                       m.id as member_id, m.name, m.email, m.password, m.role,
                       rt.id as reservation_time_id, rt.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                inner join reservation_time rt ON rs.time_id = rt.id
                inner join theme th ON rs.theme_id = th.id
                inner join member m ON rs.member_id = m.id
                where rs.theme_id = :theme_id AND rs.member_id = :member_id AND rs.date between :date_from and :date_to
                """;
        Map<String, Object> params = Map.of(
                "theme_id", themeId,
                "member_id", memberId,
                "date_from", dateFrom,
                "date_to", dateTo
        );

        return jdbcTemplate.query(sql,
                params,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    private Optional<Reservation> reservationOf(final String sql, final Map<String, Long> params) {
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql,
                    params,
                    (resultSet, rowNum) -> reservationOf(resultSet));
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Reservation reservationOf(final ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                memberOf(resultSet),
                LocalDate.parse(resultSet.getString("date")),
                reservationTimeOf(resultSet),
                themeOf(resultSet)
        );
    }

    private Member memberOf(final ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    private ReservationTime reservationTimeOf(final ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("reservation_time_id"),
                LocalTime.parse(resultSet.getString("start_at")
                ));
    }

    private Theme themeOf(final ResultSet resultSet) throws SQLException {
        return Theme.builder()
                .id(resultSet.getLong("theme_id"))
                .name(resultSet.getString("theme_name"))
                .description(resultSet.getString("description"))
                .thumbnail(resultSet.getString("thumbnail"))
                .build();
    }
}

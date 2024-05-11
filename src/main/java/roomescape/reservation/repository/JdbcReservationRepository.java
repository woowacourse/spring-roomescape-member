package roomescape.reservation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.model.Member;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

@Repository
public class JdbcReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final RowMapper<Reservation> ROW_MAPPER =
            (resultSet, rowNum) -> new Reservation(
                    resultSet.getLong("id"),
                    new Member(
                            resultSet.getLong("member_id"),
                            resultSet.getString("member_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    ),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("start_at").toLocalTime()),
                    new Theme(resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    )
            );

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(final Reservation reservation) {
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("time_id", reservation.getReservationTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        long id = simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();

        return new Reservation(
                id,
                reservation.getMember(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
    }

    public List<Reservation> findAll() {
        String sql = """
                select r.id, r.date, 
                        rt.id as time_id, rt.start_at, 
                        t.id as theme_id, t.name as theme_name, t.description, t.thumbnail,
                        m.id as member_id, m.name as member_name, m.email, m.password 
                from reservation as r 
                join reservation_time as rt on r.time_id = rt.id
                join theme as t on r.theme_id = t.id
                join member as m on r.member_id = m.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<Reservation> findById(final Long id) {
        String sql = """
                select r.id, r.date,
                        rt.id as time_id, rt.start_at, 
                        t.id as theme_id, t.name as theme_name, t.description, t.thumbnail,
                        m.id as member_id, m.name as member_name, m.email, m.password
                from reservation as r 
                join reservation_time as rt on r.time_id = rt.id
                join theme as t on r.theme_id = t.id 
                join member as m on r.member_id = m.id
                where r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    public List<Reservation> findAllByTimeId(final Long timeId) {
        String sql = """
                select r.id, r.date, 
                    rt.id as time_id, rt.start_at,
                    t.id as theme_id, t.name as theme_name, t.description, t.thumbnail,
                    m.id as member_id, m.name as member_name, m.email, m.password
                from reservation as r
                join reservation_time as rt on r.time_id = rt.id
                join theme as t r.theme_id = t.id
                join member as m on r.member_id = m.id
                where r.time_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, timeId);
    }

    public List<Reservation> findAllByThemeId(final Long themeId) {
        String sql = """
                select r.id, r.date,
                    rt.id as time_id, rt.start_at,
                    t.id as theme_id, t.name as theme_name, t.description, t.thumbnail,
                    m.id as member_id, m.name as member_name, m.email, m.password
                from reservation as r
                join reservation_time as rt on r.time_id = rt.id
                join theme as t on r.theme_id = t.id
                join member as m on r.member_id = m.id 
                where r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, themeId);
    }

    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = """
                select r.id, r.date,
                    rt.id as time_id, rt.start_at,
                    t.id as theme_id, t.name as theme_name, t.description, t.thumbnail,
                    m.id as member_id, m.name as member_name, m.email, m.password
                from reservation as r
                join reservation_time as rt on r.time_id = rt.id
                join theme as t on r.theme_id = t.id 
                join member as m on r.member_id = m.id 
                where r.date = ? and r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }

    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final Long timeId, final Long themeId) {
        String sql = """
                select exists (
                    select 1
                    from reservation as r
                    join reservation_time as rt on r.time_id = rt.id
                    join theme as t on r.theme_id = t.id
                    where r.date = ? and r.time_id = ? and r.theme_id = ?)
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    public void deleteById(final Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}

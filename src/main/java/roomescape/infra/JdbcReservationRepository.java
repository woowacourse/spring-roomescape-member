package roomescape.infra;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.entity.Role;
import roomescape.domain.entity.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.error.NotFoundException;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    new Member(resultSet.getLong("member_id"), resultSet.getString("member_name"),
                            resultSet.getString("member_email"), resultSet.getString("member_password"),
                            Role.valueOf(resultSet.getString("member_role"))),
                    resultSet.getObject("date", LocalDate.class),
                    new ReservationTime(resultSet.getLong("time_id"), LocalTime.parse(resultSet.getString("time_value"))),
                    new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
            );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcReservationRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("reservation")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "date", "time_id", "theme_id");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select 
                    r.id, 
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    m.role as member_role,
                    t.id as time_id, 
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                from reservation r
                inner join member m on r.member_id = m.id
                inner join reservation_time t on r.time_id = t.id
                inner join theme th on r.theme_id = th.id
                """;
        return template.query(sql, reservationRowMapper);
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final LocalTime time, final Long themeId) {
        final String existsSql = """
                    select exists(
                        select 1
                        from reservation as r
                        inner join reservation_time as rt
                        on r.time_id = rt.id
                        where r.date = ?
                          and rt.start_at = ?
                          and r.theme_id = ?
                    )
                """;
        final Boolean exists = template.queryForObject(existsSql, Boolean.class, date, time, themeId);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean existsByReservationTimeId(final Long reservationTimeId) {
        final String existsSql = """
                    select exists(
                        select 1
                        from reservation as r
                        where r.time_id = ?
                    )
                """;
        final Boolean exists = template.queryForObject(existsSql, Boolean.class, reservationTimeId);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        final String existsSql = """
                    select exists(
                        select 1
                        from reservation as r
                        where r.theme_id = ?
                    )
                """;
        final Boolean exists = template.queryForObject(existsSql, Boolean.class, themeId);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        final long newId = inserter.executeAndReturnKey(params).longValue();
        return new Reservation(newId, reservation.getMember(),
                reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = """
                select 
                    r.id, 
                    r.date,
                    m.id as member_id,
                    m.name as member_name,
                    m.email as member_email,
                    m.password as member_password,
                    m.role as member_role,
                    t.id as time_id, 
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                from reservation r
                inner join member m on r.member_id = m.id
                inner join reservation_time t on r.time_id = t.id
                inner join theme th on r.theme_id = th.id
                where r.id = ?
                """;
        return template.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from reservation where id = ?";
        final int rows = template.update(sql, id);
        if (rows == 0) {
            throw new NotFoundException("삭제하려고 하는 예약이 존재하지 않습니다. " + id);
        }
    }

    @Override
    public List<Reservation> search(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        final StringBuilder sql = new StringBuilder("""
                    select r.id,
                           r.date,
                           m.id as member_id,
                           m.name as member_name,
                           m.email as member_email,
                           m.password as member_password,
                           m.role as member_role,
                           t.id as time_id,
                           t.start_at as time_value,
                           th.id as theme_id,
                           th.name as theme_name,
                           th.description as theme_description,
                           th.thumbnail as theme_thumbnail
                    from reservation r
                    inner join member m on r.member_id = m.id
                    inner join reservation_time t on r.time_id = t.id
                    inner join theme th on r.theme_id = th.id
                    where 1=1
                """);

        final List<Object> args = new ArrayList<>();
        if (themeId != null) {
            sql.append(" and r.theme_id = ?");
            args.add(themeId);
        }
        if (memberId != null) {
            sql.append(" and m.id = ?");
            args.add(memberId);
        }
        if (dateFrom != null) {
            sql.append(" and r.date >= ?");
            args.add(dateFrom);
        }
        if (dateTo != null) {
            sql.append(" and r.date <= ?");
            args.add(dateTo);
        }

        return template.query(sql.toString(), reservationRowMapper, args.toArray());
    }

}

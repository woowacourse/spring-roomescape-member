package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail_url")
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("reservation_date").toLocalDate(),
                time,
                theme,
                ReservationStatus.valueOf(resultSet.getString("reservation_status"))
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "insert into reservation (name, reservation_date, time_id, theme_id, status) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            ps.setString(5, reservation.getStatus().name());
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId)
                .orElseThrow(() -> new IllegalStateException("서버 오류: 데이터 저장 직후 조회가 실패했습니다. (ID: " + generatedId + ")"));
    }

    @Override
    public void cancelById(Long id) {
        String sql = "update reservation set status = 'CANCELED' where id = ? and status = 'RESERVED'";
        int updated = jdbcTemplate.update(sql, id);

        if (updated == 0) {
            throw new ResourceNotFoundException("해당 예약을 찾을 수 없거나 이미 취소되었습니다.");
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
            select
                r.id as reservation_id,
                r.name as reservation_name,
                r.reservation_date,
                r.status as reservation_status,
                r.time_id,
                t.start_at as time_start_at,
                h.id as theme_id,
                h.name as theme_name,
                h.description as theme_description,
                h.thumbnail_url as theme_thumbnail_url
            from reservation r
            inner join reservation_time t on r.time_id = t.id
            inner join theme h on r.theme_id = h.id
            where r.id = ?
            """;

        List<Reservation> results = jdbcTemplate.query(sql, reservationRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<Reservation> findAll(LocalDateTime now) {
        String sql = """
            select
                r.id as reservation_id,
                r.name as reservation_name,
                r.reservation_date,
                case 
                    when r.status = 'RESERVED' and (r.reservation_date < ? or (r.reservation_date = ? and t.start_at <= ?))
                        then 'COMPLETED'
                    else r.status
                end as reservation_status,
                r.time_id,
                t.start_at as time_start_at,
                h.id as theme_id,
                h.name as theme_name,
                h.description as theme_description,
                h.thumbnail_url as theme_thumbnail_url
            from reservation r
            inner join reservation_time t on r.time_id = t.id
            inner join theme h on r.theme_id = h.id
            order by r.id DESC
            """;
        return jdbcTemplate.query(sql, reservationRowMapper,
                Date.valueOf(now.toLocalDate()),
                Date.valueOf(now.toLocalDate()),
                Time.valueOf(now.toLocalTime())
        );
    }

    @Override
    public List<Reservation> findByFilter(String name, LocalDate from, LocalDate to, Long themeId, LocalDateTime now) {
        List<Object> params = new ArrayList<>();

        params.add(Date.valueOf(now.toLocalDate()));
        params.add(Date.valueOf(now.toLocalDate()));
        params.add(Time.valueOf(now.toLocalTime()));

        StringBuilder sql = new StringBuilder("""
        select
            r.id              as reservation_id,
            r.name            as reservation_name,
            r.reservation_date,
                case 
                    when r.status = 'RESERVED' and (r.reservation_date < ? or (r.reservation_date = ? and t.start_at <= ?))
                        then 'COMPLETED'
                    else r.status
                end as reservation_status,
            r.time_id,
            t.start_at        as time_start_at,
            h.id              as theme_id,
            h.name            as theme_name,
            h.description     as theme_description,
            h.thumbnail_url   as theme_thumbnail_url
        from reservation r
        inner join reservation_time t on r.time_id  = t.id
        inner join theme            h on r.theme_id = h.id
        where 1=1
        """);

        if (name != null) {
            sql.append(" and r.name = ?");
            params.add(name);
        }

        if (from != null) {
            sql.append(" and r.reservation_date >= ?");
            params.add(Date.valueOf(from));
        }
        if (to != null) {
            sql.append(" and r.reservation_date <= ?");
            params.add(Date.valueOf(to));
        }
        if (themeId != null) {
            sql.append(" and r.theme_id = ?");
            params.add(themeId);
        }
        sql.append(" order by r.reservation_date desc, t.start_at desc");

        return jdbcTemplate.query(sql.toString(), reservationRowMapper, params.toArray());
    }

    @Override
    public void update(Reservation reservation) {
        String sql = """
        update reservation
           set name = ?, 
               reservation_date = ?,
               time_id = ?,
               theme_id = ?
         where id = ?
           and status = 'RESERVED'
        """;
        int updated = jdbcTemplate.update(
                sql,
                reservation.getName(),
                Date.valueOf(reservation.getDate()),
                reservation.getTime().getId(),
                reservation.getTheme().getId(),
                reservation.getId()
        );

        if (updated == 0) {
            throw new ResourceNotFoundException(
                    "수정하려는 예약을 찾을 수 없거나 이미 취소/완료되었습니다. (ID: " + reservation.getId() + ")");
        }
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeIdExcluding(
            LocalDate date, Long timeId, Long themeId, Long excludeId
    ) {
        String sql = """
        select exists (
            select 1 from reservation
            where reservation_date = ?
              and time_id  = ?
              and theme_id = ?
              and status   = 'RESERVED'
              and id != ?
        )
        """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId, excludeId);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
        select exists (
            select 1 from reservation
            where reservation_date = ? and time_id = ? and theme_id = ?
              and status = 'RESERVED'
        )
        """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "select exists (select 1 from reservation where time_id = ? and status = 'RESERVED')";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "select exists (select 1 from reservation where theme_id = ? and status = 'RESERVED')";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public int completeAllPastReservations(LocalDateTime now) {
        String sql = """
            update reservation
            set status = 'COMPLETED'
            where status = 'RESERVED'
              and id in (
                select r.id from reservation r
                join reservation_time t on r.time_id = t.id
                where r.reservation_date < ?
                   or (r.reservation_date = ? and t.start_at <= ?)
              )
            """;
        return jdbcTemplate.update(sql,
                Date.valueOf(now.toLocalDate()),
                Date.valueOf(now.toLocalDate()),
                now.toLocalTime()
        );
    }
}

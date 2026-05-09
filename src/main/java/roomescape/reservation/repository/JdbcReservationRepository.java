package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
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
                theme
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "insert into reservation (name, reservation_date, time_id, theme_id) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId)
                .orElseThrow(() -> new IllegalStateException("서버 오류: 데이터 저장 직후 조회가 실패했습니다. (ID: " + generatedId + ")"));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from reservation";
        jdbcTemplate.update(sql);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
            select
                r.id as reservation_id,
                r.name as reservation_name,
                r.reservation_date,
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
    public List<Reservation> findAll() {
        String sql = """
            select
                r.id as reservation_id,
                r.name as reservation_name,
                r.reservation_date,
                r.time_id,
                t.start_at as time_start_at,
                h.id as theme_id,
                h.name as theme_name,
                h.description as theme_description,
                h.thumbnail_url as theme_thumbnail_url
            from reservation r
            inner join reservation_time t on r.time_id = t.id
            inner join theme h on r.theme_id = h.id
            order by r.id
            """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public List<Reservation> findByFilter(LocalDate date, Long themeId) {
        StringBuilder sql = new StringBuilder("""
            select
                r.id as reservation_id,
                r.name as reservation_name,
                r.reservation_date,
                r.time_id,
                t.start_at as time_start_at,
                h.id as theme_id,
                h.name as theme_name,
                h.description as theme_description,
                h.thumbnail_url as theme_thumbnail_url
            from reservation r
            inner join reservation_time t on r.time_id = t.id
            inner join theme h on r.theme_id = h.id
            where 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (date != null) {
            sql.append(" and r.reservation_date = ?");
            params.add(Date.valueOf(date));
        }
        if (themeId != null) {
            sql.append(" and r.theme_id = ?");
            params.add(themeId);
        }
        sql.append(" order by r.id");

        return jdbcTemplate.query(sql.toString(), reservationRowMapper, params.toArray());
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "select exists (select 1 from reservation where reservation_date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}

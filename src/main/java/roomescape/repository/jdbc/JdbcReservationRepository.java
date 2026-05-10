package roomescape.repository.jdbc;

import static roomescape.repository.jdbc.ReservationEntityMapper.RESERVATION_ROW_MAPPER;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation (name, date, theme_id, time_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTheme().getId());
            ps.setLong(4, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Reservation(id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme(),
                reservation.getTime());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    @Override
    public boolean existByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                    SELECT r.id AS res_id, r.name AS res_name, r.date AS res_date,
                           rt.id AS time_id, rt.start_at AS time_start,
                           t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail_image_url, t.is_active
                    FROM reservation r
                    JOIN reservation_time rt ON r.time_id = rt.id
                    JOIN theme t ON r.theme_id = t.id
                """;
        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }
}

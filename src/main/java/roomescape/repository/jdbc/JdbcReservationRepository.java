package roomescape.repository.jdbc;

import static roomescape.repository.jdbc.ReservationEntityMapper.RESERVATION_ROW_MAPPER;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.repository.ReservationRepository;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation (name, date, theme_id, time_id, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTheme().getId());
            ps.setLong(4, reservation.getTime().getId());
            ps.setString(5, reservation.getStatus().name());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Reservation(id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme(),
                reservation.getTime(),
                reservation.getStatus()
        );
    }

    @Override
    public void update(Reservation reservation) {
        String sql = """
                    UPDATE reservation
                    SET name = ?, date = ?, theme_id = ?, time_id = ?, status = ?
                    WHERE id=?
                """;

        jdbcTemplate.update(sql,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme().getId(),
                reservation.getTime().getId(),
                reservation.getStatus().toString(),
                reservation.getId()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        try {
            String sql = """
                    SELECT r.id AS res_id, r.name AS res_name, r.date AS res_date, r.status AS res_status,
                           rt.id AS time_id, rt.start_at AS time_start,
                           t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail_image_url, t.is_active
                    FROM reservation r
                    JOIN reservation_time rt ON r.time_id = rt.id
                    JOIN theme t ON r.theme_id = t.id
                    WHERE r.id = ?
                    """;

            Reservation reservation = jdbcTemplate.queryForObject(sql, RESERVATION_ROW_MAPPER, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        int affectedRow = jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
        if (affectedRow == 0) {
            throw new EntityNotFoundException("존재하지 않는 예약 정보입니다.");
        }
    }

    @Override
    public boolean existsReservedReservation(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ? AND status = 'RESERVED')";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public List<Reservation> findAllByPaging(int page, int size) {
        int offset = page * size;
        String sql = """
                    SELECT r.id AS res_id, r.name AS res_name, r.date AS res_date, r.status AS res_status,
                           rt.id AS time_id, rt.start_at AS time_start,
                           t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail_image_url, t.is_active
                    FROM reservation r
                    JOIN reservation_time rt ON r.time_id = rt.id
                    JOIN theme t ON r.theme_id = t.id
                    ORDER BY r.id DESC
                    LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, size, offset);
    }

    @Override
    public Set<Long> findUnavailableTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                SELECT time_id
                FROM reservation
                WHERE theme_id = ? AND date = ? AND status = 'RESERVED' 
                """;

        return new HashSet<>(jdbcTemplate.queryForList(sql, Long.class, themeId, date));
    }

    @Override
    public List<Reservation> findAllByUserName(String name) {
        String sql = """
                    SELECT r.id AS res_id, r.name AS res_name, r.date AS res_date, r.status AS res_status,
                           rt.id AS time_id, rt.start_at AS time_start,
                           t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail_image_url, t.is_active
                    FROM reservation r
                    JOIN reservation_time rt ON r.time_id = rt.id
                    JOIN theme t ON r.theme_id = t.id
                    WHERE r.name = ?
                    ORDER BY r.id DESC
                """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, name);
    }
}

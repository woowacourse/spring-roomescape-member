package roomescape.persistence.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.Reservation;
import roomescape.persistence.entity.ReservationEntity;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Reservation reservation) {
        final ReservationEntity reservationEntity = ReservationEntity.from(reservation);
        final String sql = "INSERT INTO RESERVATION (name, date, time_id) values (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationEntity.name());
            ps.setString(2, reservationEntity.date());
            ps.setLong(3, reservationEntity.playTimeEntity().id());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql =
                """
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                """;

        return jdbcTemplate.query(sql, ReservationEntity.getDefaultRowMapper()).stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        final String sql = "DELETE FROM RESERVATION WHERE id = ?";
        final int rowNum = jdbcTemplate.update(sql, id);

        return rowNum == 1;
    }
}

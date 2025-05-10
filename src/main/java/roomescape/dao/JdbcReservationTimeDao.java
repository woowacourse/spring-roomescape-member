package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.entity.ReservationTime;
import roomescape.mapper.ReservationTimeMapper;

@Component
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(
                sql,
                new ReservationTimeMapper()
        );
    }

    public ReservationTime findById(Long id) {
        try {
            String sql = "select id, start_at from reservation_time where id = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    new ReservationTimeMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 시간 id입니다.");
        }
    }

    public ReservationTime create(ReservationTime time) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setObject(1, time.getStartAt());
                    return ps;
                }, keyHolder
        );
        long reservationTimeId = keyHolder.getKey().longValue();
        return time.copyWithId(reservationTimeId);
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        int deletedCount = jdbcTemplate.update(
                sql,
                id
        );
        if (deletedCount == 0) {
            throw new IllegalArgumentException("존재하지 않는 시간 id입니다.");
        }
    }

    @Override
    public boolean existsByStartAt(ReservationTime reservationTime) {
        String sql = "select exists (select 1 from reservation_time where start_at = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                reservationTime.getStartAt()
        ));
    }
}

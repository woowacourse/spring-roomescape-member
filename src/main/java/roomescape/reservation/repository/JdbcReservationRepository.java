package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcReservationRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("reservation")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "date", "time_id");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select 
                    r.id as reservation_id, 
                    r.name, r.date, 
                    t.id as time_id, 
                    t.start_at as time_value 
                from reservation as r
                inner join reservation_time as t 
                on r.time_id = t.id
                """;
        return template.query(sql, reservationRowMapper);
    }

    @Override
    public boolean existsByDateAndTime(final LocalDate date, final LocalTime time) {
        final String sql = """
                select count(*) 
                from reservation as r                
                inner join reservation_time as rt 
                where r.date = ? 
                  and rt.start_at = ?
                """;
        final Integer count = template.queryForObject(sql, Integer.class, date, time);
        return count != null && count > 0;
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId());

        final long newId = inserter.executeAndReturnKey(params).longValue();
        return new Reservation(newId, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = """
                 select 
                    r.id as reservation_id, 
                    r.name, r.date, 
                    t.id as time_id, 
                    t.start_at as time_value 
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
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
            throw new NoSuchElementException("삭제하려고 하는 예약이 존재하지 않습니다. " + id);
        }
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        final LocalTime time = LocalTime.parse(resultSet.getString("time_value"), timeFormatter);

        return new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                new ReservationTime(resultSet.getLong("id"), time)
        );
    };
}

package roomescape.domain.reservationdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcReservationDateRepository implements ReservationDateRepository{

    private static final String FIND_BY_ID_SQL = "select id, `date` from reservation_date where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<ReservationDate> findById(Long id) {
        List<ReservationDate> result = jdbcTemplate.query(FIND_BY_ID_SQL, reservationDateRowMapper(), id);
        return result.stream().findFirst();
    }

    private RowMapper<ReservationDate> reservationDateRowMapper() {
        return (rs, rowNum) -> ReservationDate.of(
            rs.getLong("id"),
            LocalDate.parse(rs.getString("date"))
        );
    }
}

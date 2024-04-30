package roomescape.infrastructure.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;

public class ReservationRowMapper implements RowMapper<Reservation> {

    private static final ReservationRowMapper INSTANCE = new ReservationRowMapper();

    private ReservationRowMapper() {
    }

    public static ReservationRowMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReservationTimeRowMapper reservationTimeRowMapper = ReservationTimeRowMapper.getInstance();
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String date = rs.getString("date");
        return new Reservation(
                id,
                new PlayerName(name),
                LocalDate.parse(date),
                reservationTimeRowMapper.mapRow(rs, rowNum)
        );
    }
}

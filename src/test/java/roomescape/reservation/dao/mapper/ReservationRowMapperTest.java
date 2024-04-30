package roomescape.reservation.dao.mapper;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationRowMapperTest {

    private final ReservationRowMapper reservationRowMapper = new ReservationRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("reservation_id", Types.BIGINT, 10, 0);
        rs.addColumn("name", Types.VARCHAR, 255, 0);
        rs.addColumn("date", Types.VARCHAR, 255, 0);
        rs.addColumn("time_id", Types.BIGINT, 10, 0);
        rs.addColumn("time_value", Types.VARCHAR, 255, 0);
        rs.addRow(1, "hotea", LocalDate.MAX.toString(), 1, "10:00");
        rs.next();

        Reservation reservation = new Reservation(1L, "hotea", LocalDate.MAX.toString(), new ReservationTime(1L, "10:00"));
        assertThat(reservationRowMapper.mapRow(rs, 1)).isEqualTo(reservation);
    }
}

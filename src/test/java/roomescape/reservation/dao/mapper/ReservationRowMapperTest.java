package roomescape.reservation.dao.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationRowMapperTest {

    private final ReservationRowMapper reservationRowMapper = new ReservationRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("reservation_id", Types.BIGINT, 10, 0);
        rs.addColumn("reservation_name", Types.VARCHAR, 255, 0);
        rs.addColumn("date", Types.VARCHAR, 255, 0);
        rs.addColumn("time_id", Types.BIGINT, 10, 0);
        rs.addColumn("time_value", Types.VARCHAR, 255, 0);
        rs.addColumn("theme_id", Types.BIGINT, 10, 0);
        rs.addColumn("theme_name", Types.VARCHAR, 255, 0);
        rs.addColumn("description", Types.VARCHAR, 255, 0);
        rs.addColumn("thumbnail", Types.VARCHAR, 255, 0);
        rs.addRow(1, "hotea", LocalDate.MAX.toString(), 1, "10:00", 1, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        rs.next();

        Reservation reservation = Reservation.of(1L, "hotea", LocalDate.MAX.toString(),
                ReservationTime.of(1L, "10:00"),
                new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
        assertThat(reservationRowMapper.mapRow(rs, 1)).isEqualTo(reservation);
    }
}

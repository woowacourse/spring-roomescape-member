package roomescape.repository.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.SQLException;
import java.sql.Types;
import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;

class ReservationRowMapperTest {

    @DisplayName("RowMapper는 ResultSet을 이용하여 Reservation을 매핑한다.")
    @Test
    void return_reservation_by_map_row() throws SQLException {
        SimpleResultSet resultSet = new SimpleResultSet();
        resultSet.addColumn("reservation_id", Types.BIGINT, 10, 0);
        resultSet.addColumn("name", Types.VARCHAR, 255, 0);
        resultSet.addColumn("date", Types.VARCHAR, 255, 0);
        resultSet.addColumn("time_id", Types.BIGINT, 10, 0);
        resultSet.addColumn("time_value", Types.VARCHAR, 255, 0);
        resultSet.addRow(1, "재즈", "2024-04-24", 1, "20:00");
        resultSet.next();

        ReservationRowMapper rowMapper = new ReservationRowMapper();
        Reservation reservation = rowMapper.mapRow(resultSet, 1);

        assertAll(
                () -> assertThat(reservation.getId()).isEqualTo(1),
                () -> assertThat(reservation.getName()).isEqualTo("재즈"),
                () -> assertThat(reservation.getDate()).isEqualTo("2024-04-24"),
                () -> assertThat(reservation.getTimeId()).isEqualTo(1),
                () -> assertThat(reservation.getTimeStartAt()).isEqualTo("20:00")
        );
    }
}

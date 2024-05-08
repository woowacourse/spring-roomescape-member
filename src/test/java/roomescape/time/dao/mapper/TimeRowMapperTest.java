package roomescape.time.dao.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;

import roomescape.time.domain.ReservationTime;

class TimeRowMapperTest {

    private final TimeRowMapper timeRowMapper = new TimeRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.BIGINT, 10, 0);
        rs.addColumn("start_at", Types.VARCHAR, 255, 0);
        rs.addRow(1, "23:59");
        rs.next();

        ReservationTime reservationTime = new ReservationTime(1L, "23:59");
        assertThat(timeRowMapper.mapRow(rs, 1)).isEqualTo(reservationTime);
    }
}

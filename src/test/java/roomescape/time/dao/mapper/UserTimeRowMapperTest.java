package roomescape.time.dao.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;

import roomescape.time.domain.ReservationUserTime;

class UserTimeRowMapperTest {
    private final UserTimeRowMapper userTimeRowMapper = new UserTimeRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.BIGINT, 10, 0);
        rs.addColumn("start_at", Types.VARCHAR, 255, 0);
        rs.addColumn("already_booked", Types.BOOLEAN, 1, 0);
        rs.addRow(1, "10:00", false);
        rs.next();

        ReservationUserTime reservationUserTime = new ReservationUserTime(1L, "10:00", false);
        assertThat(userTimeRowMapper.mapRow(rs, 1)).isEqualTo(reservationUserTime);
    }
}

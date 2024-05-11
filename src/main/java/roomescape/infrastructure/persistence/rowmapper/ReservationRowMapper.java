package roomescape.infrastructure.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public class ReservationRowMapper {

    private ReservationRowMapper() {
    }

    public static Reservation joinedMapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String date = rs.getString("date");
        return new Reservation(
                id,
                MemberRowMapper.joinedMapRow(rs),
                LocalDate.parse(date),
                ReservationTimeRowMapper.joinedMapRow(rs),
                ThemeRowMapper.joinedMapRow(rs)
        );
    }
}

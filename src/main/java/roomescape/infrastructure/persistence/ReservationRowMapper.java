package roomescape.infrastructure.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;

public class ReservationRowMapper {

    private ReservationRowMapper() {
    }

    public static Reservation joinedMapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String date = rs.getString("date");
        return new Reservation(
                id,
                new PlayerName(name),
                LocalDate.parse(date),
                ReservationTimeRowMapper.joinedMapRow(rs),
                ThemeRowMapper.joinedMapRow(rs)
        );
    }
}

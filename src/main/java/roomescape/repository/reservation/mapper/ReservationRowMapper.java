package roomescape.repository.reservation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

public class ReservationRowMapper implements RowMapper<Reservation> {

    public static final ReservationRowMapper INSTANCE = new ReservationRowMapper();

    private ReservationRowMapper() {
    }

    @Override
    public Reservation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Reservation.of(
                resultSet.getLong("reservation_id"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                )
        );
    }
}

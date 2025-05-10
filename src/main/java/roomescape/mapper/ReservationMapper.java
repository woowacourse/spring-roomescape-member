package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

public class ReservationMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Reservation(
                rs.getLong("id"),
                rs.getObject("date", LocalDate.class),
                new Member(
                        rs.getLong("member_id"),
                        rs.getString("member_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        MemberRole.from(rs.getString("role"))
                ),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getObject("start_at", LocalTime.class)
                ),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }
}

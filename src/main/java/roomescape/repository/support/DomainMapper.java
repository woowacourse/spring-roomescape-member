package roomescape.repository.support;

import org.springframework.jdbc.core.RowMapper;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

public class DomainMapper {

    public static RowMapper<Member> MEMBER =
            (rs, rowNum) -> new Member(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL"),
                    rs.getString("PASSWORD"),
                    Role.findByName(rs.getString("ROLE"))
            );

    public static RowMapper<TimeSlot> TIMESLOT =
            (rs, rowNum) -> new TimeSlot(
                    rs.getLong("ID"),
                    rs.getTime("START_AT").toLocalTime()
            );

    public static RowMapper<Theme> THEME =
            (rs, rowNum) -> new Theme(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getString("DESCRIPTION"),
                    rs.getString("THUMBNAIL")
            );

    public static RowMapper<Reservation> RESERVATION =
            (rs, rowNum) -> new Reservation(
                    rs.getLong("R_ID"),
                    new Member(
                            rs.getLong("M_ID"),
                            rs.getString("M_NAME"),
                            rs.getString("M_EMAIL"),
                            rs.getString("M_PASSWORD"),
                            Role.findByName(rs.getString("M_ROLE"))
                    ),
                    rs.getDate("R_DATE").toLocalDate(),
                    new TimeSlot(
                            rs.getLong("RT_ID"),
                            rs.getTime("RT_START_AT").toLocalTime()
                    ),
                    new Theme(
                            rs.getLong("T_ID"),
                            rs.getString("T_NAME"),
                            rs.getString("T_DESCRIPTION"),
                            rs.getString("T_THUMBNAIL")
                    )
            );
}

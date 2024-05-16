package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("reservation_id");

        Long memberId = resultSet.getLong("member_id");
        String memberName = resultSet.getString("member_name");
        String memberRole = resultSet.getString("member_role");
        String memberEmail = resultSet.getString("member_email");
        String memberPassword = resultSet.getString("member_password");

        Long themeId = resultSet.getLong("theme_id");
        String themeName = resultSet.getString("theme_name");
        String themeDescription = resultSet.getString("theme_description");
        String themeThumbnail = resultSet.getString("theme_thumbnail");

        String date = resultSet.getString("date");

        Long timeId = resultSet.getLong("time_id");
        String time = resultSet.getString("time_value");

        Member member = new Member(memberId, memberName, memberRole, memberEmail, memberPassword);
        Theme theme = new Theme(themeId, themeName, themeDescription, themeThumbnail);
        ReservationDate reservationDate = new ReservationDate(date);
        ReservationTime reservationTime = new ReservationTime(timeId, time);

        return new Reservation(id, member, theme, reservationDate, reservationTime);
    }
}

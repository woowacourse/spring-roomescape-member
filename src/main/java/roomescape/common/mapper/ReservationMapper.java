package roomescape.common.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public class ReservationMapper implements RowMapper<Reservation> {
    @Override
    public Reservation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Reservation(
                resultSet.getLong("id"),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_name"),
                        resultSet.getString("member_email"),
                        Role.valueOf(resultSet.getString("member_role"))
                ),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
    }
}

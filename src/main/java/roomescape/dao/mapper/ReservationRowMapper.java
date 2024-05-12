package roomescape.dao.mapper;

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
    public Reservation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final var reservationId = rs.getLong("reservation_id");
        final var date = rs.getString("date");

        final var timeId = rs.getLong("time_id");
        final var startAt = rs.getString("start_at");

        final var themeId = rs.getLong("theme_id");
        final var themeName = rs.getString("theme_name");
        final var description = rs.getString("theme_description");
        final var thumbnail = rs.getString("theme_thumbnail");

        final var memberId = rs.getLong("member_id");
        final var memberName = rs.getString("member_name");
        final var memberEmail = rs.getString("member_email");
        final var memberPassword = rs.getString("member_password");
        final var memberRole = rs.getString("member_role");

        return new Reservation(
                reservationId,
                Member.of(memberId, memberName, memberEmail, memberPassword, memberRole),
                ReservationDate.from(date),
                ReservationTime.of(timeId, startAt),
                Theme.of(themeId, themeName, description, thumbnail)
        );
    }
}

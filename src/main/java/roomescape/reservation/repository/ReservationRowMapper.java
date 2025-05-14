package roomescape.reservation.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.auth.domain.Role;
import roomescape.member.entity.MemberEntity;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservationtime.entity.ReservationTimeEntity;
import roomescape.theme.entity.ThemeEntity;

public class ReservationRowMapper implements RowMapper<Reservation> {

    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReservationTimeEntity timeEntity = new ReservationTimeEntity(
                rs.getLong("time_id"),
                rs.getString("time_value")
        );

        ThemeEntity themeEntity = new ThemeEntity(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        );

        MemberEntity memberEntity = new MemberEntity(
                rs.getLong("member_id"),
                rs.getString("member_name"),
                rs.getString("member_email"),
                rs.getString("member_password"),
                Role.valueOf(rs.getString("member_role"))
        );

        ReservationEntity entity = new ReservationEntity(
                rs.getLong("reservation_id"),
                rs.getString("date"),
                memberEntity,
                timeEntity,
                themeEntity
        );

        return entity.toReservation();
    }
}

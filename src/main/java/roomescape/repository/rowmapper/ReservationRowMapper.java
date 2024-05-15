package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    private final RowMapper<ReservationTime> reservationTimeRowMapper;
    private final RowMapper<Theme> themeRowMapper;
    private final RowMapper<Member> memberRowMapper;

    public ReservationRowMapper(
            final RowMapper<ReservationTime> reservationTimeRowMapper,
            final RowMapper<Theme> themeRowMapper,
            final RowMapper<Member> memberRowMapper
    ) {
        this.reservationTimeRowMapper = reservationTimeRowMapper;
        this.themeRowMapper = themeRowMapper;
        this.memberRowMapper = memberRowMapper;
    }

    @Override
    public Reservation mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getObject("date", LocalDate.class),
                    reservationTimeRowMapper.mapRow(resultSet, rowNumber),
                    themeRowMapper.mapRow(resultSet, rowNumber),
                    memberRowMapper.mapRow(resultSet, rowNumber)
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

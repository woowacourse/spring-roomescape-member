package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.roomescape.Reservation;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    private final ReservationTimeRowMapper reservationTimeRowMapper;
    private final ThemeRowMapper themeRowMapper;
    private final MemberRowMapper memberRowMapper;

    public ReservationRowMapper(
            final ReservationTimeRowMapper reservationTimeRowMapper,
            final ThemeRowMapper themeRowMapper,
            final MemberRowMapper memberRowMapper
    ) {
        this.reservationTimeRowMapper = reservationTimeRowMapper;
        this.themeRowMapper = themeRowMapper;
        this.memberRowMapper = memberRowMapper;
    }

    @Override
    public Reservation mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return Reservation.of(
                    resultSet.getLong("id"),
                    memberRowMapper.mapRow(resultSet, rowNumber),
                    resultSet.getString("date"),
                    reservationTimeRowMapper.mapRow(resultSet, rowNumber),
                    themeRowMapper.mapRow(resultSet, rowNumber)
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

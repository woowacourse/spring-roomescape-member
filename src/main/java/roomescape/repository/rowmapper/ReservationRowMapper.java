package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    private final RowMapper<ReservationTime> reservationTimeRowMapper;
    private final RowMapper<Theme> themeRowMapper;

    public ReservationRowMapper(
            final RowMapper<ReservationTime> reservationTimeRowMapper,
            final RowMapper<Theme> themeRowMapper
    ) {
        this.reservationTimeRowMapper = reservationTimeRowMapper;
        this.themeRowMapper = themeRowMapper;
    }

    @Override
    public Reservation mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            ReservationTime reservationTime = reservationTimeRowMapper.mapRow(resultSet, rowNumber);
            return Reservation.builder()
                    .id(resultSet.getLong("reservation_id"))
                    .name(resultSet.getString("name"))
                    .date(resultSet.getString("date"))
                    .time(reservationTimeRowMapper.mapRow(resultSet, rowNumber))
                    .theme(themeRowMapper.mapRow(resultSet, rowNumber))
                    .build();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

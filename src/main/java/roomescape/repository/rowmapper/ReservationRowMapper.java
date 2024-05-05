package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ReservationRowMapper implements RowMapper<Reservation> {

    private final ReservationTimeRowMapper reservationTimeRowMapper;
    private final ThemeRowMapper themeRowMapper;

    public ReservationRowMapper(final ReservationTimeRowMapper reservationTimeRowMapper, final ThemeRowMapper themeRowMapper) {
        this.reservationTimeRowMapper = reservationTimeRowMapper;
        this.themeRowMapper = themeRowMapper;
    }

    @Override
    public Reservation mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return Reservation.of(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("date"),
                    reservationTimeRowMapper.mapRow(resultSet, rowNumber),
                    themeRowMapper.mapRow(resultSet, rowNumber)
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

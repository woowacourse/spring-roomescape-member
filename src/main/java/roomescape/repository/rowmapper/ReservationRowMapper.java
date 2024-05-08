package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
            return new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getObject("name", Name.class),
                    resultSet.getObject("date", LocalDate.class),
                    reservationTimeRowMapper.mapRow(resultSet, rowNumber),
                    themeRowMapper.mapRow(resultSet, rowNumber)
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}

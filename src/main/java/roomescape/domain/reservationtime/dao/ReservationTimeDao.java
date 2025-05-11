package roomescape.domain.reservationtime.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationtime.dto.response.BookedReservationTimeResponseDto;
import roomescape.domain.reservationtime.model.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    long save(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<BookedReservationTimeResponseDto> findBooked(String date, Long themeId);
}

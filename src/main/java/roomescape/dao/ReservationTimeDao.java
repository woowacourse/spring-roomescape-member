package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.BookedReservationTimeResponseDto;

public interface ReservationTimeDao {

    List<ReservationTime> findAll();

    long save(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<BookedReservationTimeResponseDto> findBooked(String date, Long themeId);
}

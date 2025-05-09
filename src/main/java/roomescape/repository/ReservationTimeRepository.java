package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.BookedReservationTimeResponseDto;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    void save(ReservationTime reservationTime);

    void delete(Long id);

    ReservationTime findById(Long id);

    List<BookedReservationTimeResponseDto> findBooked(String date, Long themeId);

}
